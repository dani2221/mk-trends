import json

import nltk
import numpy as np
import pandas as pd
from bs4 import BeautifulSoup
import requests
import re
from sklearn.cluster import DBSCAN
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import networkx as nx
from Model.LSA.lsa_summarizer import LsaSummarizer
from Model.stopwords import _stopwords
import html
from bs4.element import Comment


def remove_media_names(words, text):
    t = text
    for word in words:
        compiled = re.compile(re.escape(word), re.IGNORECASE)
        t = compiled.sub('', t)
    return t


def clean(text):
    text = BeautifulSoup(text, "lxml").text
    text = re.sub(r'\|\|\|', r' ', text)
    text = text.replace('„', '')
    text = text.replace('“', '')
    text = text.replace('"', '')
    text = text.replace('\'', '')
    text = text.replace('-', '')
    text = text.lower()
    return text


def remove_stopwords(content):
    for word in _stopwords:
        content = re.sub(r"\b" + word + r"\b", "", content)
    return content


class TextClusterSummarize:
    def __init__(self, urls, articles=[]):
        self.articles = pd.DataFrame(articles)
        self.urls = urls

    def cluster_summarize(self, summarize_alg='text_rank'):
        if self.articles.empty and len(self.urls) == 0:
            # for later
            pass
        if self.articles.empty:
            arcs = self.scrape_articles()
            self.send_news(arcs)
        self.prepare_text()
        tfidf = self.vectorize_text()
        clusters = self.cluster_dbscan(tfidf)
        if summarize_alg == 'text_rank':
            results = self.summarize_textrank(clusters)
        else:
            results = self.summarize_lsa(clusters)
            self.send_summaries(results)

        return results

    def scrape_articles(self):
        articles = list()
        for url in self.urls:
            try:
                data = requests.get(url, headers={'User-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) '
                                                                'AppleWebKit/537.36 (KHTML, like Gecko) '
                                                                'Chrome/103.0.0.0 Safari/537.36'})
                data.encoding = 'utf-8'
                xml = data.text
                xml = html.unescape(xml)
            except Exception as e:
                print(f'Fetch error {url} {str(e)}')
                continue
            try:
                soup = BeautifulSoup(xml, features='xml')
            except Exception as e:
                print(f'Parse error {url} {str(e)}')
                continue

            for article in soup.find_all('item'):
                article_data = dict()
                article_data['source'] = url.split("//")[1].split('/')[0]
                text = self.clean_text(article.find('content').text if article.find('content') is not None else "")
                new_text = self.clean_text(article.find('content:encoded').text
                                           if article.find('content:encoded') is not None else "")
                if len(text) < len(new_text):
                    text = new_text
                new_text = self.clean_text(article.find('description').text
                                           if article.find('description') is not None else "")
                if len(text) < len(new_text):
                    text = new_text
                if len(text) < 150:
                    continue
                if article_data['source'] == 'www.slobodenpecat.mk':
                    text = text.split('100, 300')[-1].split(':..')[0]
                if article_data['source'] == 'www.makfax.com.mk':
                    text = text.split('100, 300')[-1]
                if article_data['source'] == 'www.vecer.press':
                    text = text.split('100, 1170')[-1]

                clean_text = ""
                for sentence in text.split('.'):
                    is_in = False
                    for c in 'абвгдѓежзѕијклљмнњопрстќуфхцчџшАБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЏШ':
                        if c in sentence:
                            is_in = True
                            break
                    if is_in:
                        cnt = 0
                        for c in sentence:
                            if c in '0123456789 :':
                                cnt += 1
                        if cnt > len(sentence)*0.4:
                            continue
                        should_add = True
                        while sentence[0] not in 'АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЏШ':
                            if len(sentence) < 20:
                                should_add = False
                                break
                            sentence = sentence[1:]
                        if should_add:
                            clean_text += sentence+'. '

                if len(clean_text) < 30:
                    continue
                article_data['text'] = clean_text
                # article_data['title'] = self.clean_text(article.find('title').text)
                article_data['title'] = article.find('title').text
                try:
                    article_data['link'] = article.find('link').next
                except Exception as e:
                    pass

                if '.jpg' in article.text:
                    article_data['photoUrl'] = 'http' + article.text.split('.jpg')[0].split('http')[-1] + '.jpg'
                else:
                    if '.png' in article.text:
                        article_data['photoUrl'] = 'http' + article.text.split('.png')[0].split('http')[-1] + '.png'
                    else:
                        if article.find('img') is not None:
                            article_data['photoUrl'] = article.find('img')['src']
                articles.append(article_data)

        articles = list({v['link']: v for v in articles}.values())
        self.articles = pd.DataFrame(articles)
        print(self.articles.head())
        return articles

    @staticmethod
    def tag_visible(element):
        if element.parent.name in ['style', 'script', 'head', 'title', 'meta', '[document]', 'a', 'img']:
            return False
        if isinstance(element, Comment):
            return False
        return True

    @staticmethod
    def clean_text(text):
        t = re.sub(r'[a-zA-Z]', '', text)
        last = max(t.rfind(i) for i in "абвгдѓежзѕијклљмнњопрстќуфхцчџшАБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЏШ")
        first = min(t.index(i) if i in t else float('inf') for i in
                    "абвгдѓежзѕијклљмнњопрстќуфхцчџшАБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЏШ")
        t = t[:last + 1]
        if first != float('inf'):
            t = t[first:]
        t = t.replace('<', '').replace('>', '').replace('\n', ' ') \
            .replace('\r', '').replace('&;', '').replace('\\', '').replace('/', '') \
            .replace('=', ' ').replace('-', ' ').replace('&nbsp;','') \
            .replace(';', '').replace('\\xa0', '').replace('"', '').replace("'", '')

        t = remove_media_names(
            ('е превземен од МАКФАКС', 'е превземен од А1он', 'слободен печат', '24инфо', '24 инфо', 'либертас'), t)

        t = re.sub("\s\s+", " ", t)
        return t.strip()

    def prepare_text(self):
        self.articles['clean_text'] = self.articles['text'].apply(clean)
        self.articles['clean_text'] = self.articles['clean_text'].apply(remove_stopwords)

    def vectorize_text(self):
        tf_idf_vectorizer = TfidfVectorizer()
        return tf_idf_vectorizer.fit_transform(self.articles['clean_text'])

    def cluster_dbscan(self, tf_idf, eps=0.97, min_samples=3):
        dbscan = DBSCAN(eps=eps, min_samples=min_samples)
        res = dbscan.fit_predict(tf_idf)
        self.articles['label'] = res
        return res

    def summarize_textrank(self, clusters, num_sentences=3):
        cluster_sizes = dict()
        for i in set(clusters):
            cluster_sizes[i] = len(self.articles.loc[self.articles['label'] == i])

        summaries_text_rank = []
        cl_num = 1
        for cluster in set(clusters):
            print(f'{cl_num}/{len(set(clusters))}')
            cl_num += 1
            if cluster == -1:
                continue
            sentences = []
            original_sentences = []
            for index, row in self.articles[self.articles['label'] == cluster].iterrows():
                s = nltk.sent_tokenize(row['clean_text'])
                o = nltk.sent_tokenize(row['text'])
                if len(s) == len(o):
                    sentences.append(s)
                    original_sentences.append(o)

            sentences = [y for x in sentences for y in x]
            original_sentences = [y for x in original_sentences for y in x]

            if len(sentences) == 0:
                continue
            tf_idf_vectorizor_sentences = TfidfVectorizer()
            tf_idf_sentences = tf_idf_vectorizor_sentences.fit_transform(sentences)
            sim_mat = np.zeros([len(sentences), len(sentences)])

            for i in range(len(sentences)):
                for j in range(len(sentences)):
                    if i != j:
                        sim_mat[i][j] = cosine_similarity(tf_idf_sentences[i], tf_idf_sentences[j])[0, 0]

            nx_graph = nx.from_numpy_array(sim_mat)
            scores = nx.pagerank(nx_graph)
            ranked_sentences = ((scores[i], s, tf_idf_sentences[i]) for i, s in enumerate(original_sentences))
            ranked_sentences = sorted(ranked_sentences, key=lambda item: item[0], reverse=True)

            counter = 0
            prev_vecs = []
            summarized = ""
            for score, sentence, vec in ranked_sentences:
                if counter == num_sentences:
                    break
                is_similar = False
                if i == len(original_sentences):
                    break
                for prev_vec in prev_vecs:
                    sim = cosine_similarity(vec, prev_vec)[0, 0]
                    if sim > 0.7:
                        is_similar = True
                        break
                if not is_similar:
                    summarized += ' ' + sentence
                    prev_vecs.append(vec)
                    counter += 1

            summaries_text_rank.append(
                {'text': summarized, 'cluster': int(cluster), 'popularity': int(cluster_sizes[cluster])})

        return sorted(summaries_text_rank, key=lambda k: k['popularity'], reverse=True)

    def summarize_lsa(self, clusters, num_sentences=3):

        cluster_sizes = dict()
        for i in set(clusters):
            cluster_sizes[i] = len(self.articles.loc[self.articles['label'] == i])

        nltk.download("punkt", quiet=True)
        summaries = []
        cl_num = 1
        output = self.articles.groupby(['label'], as_index=False).agg({'text': ' '.join})
        for cluster in set(clusters):
            print(f'{cl_num}/{len(set(clusters))}')
            cl_num += 1
            if cluster == -1:
                continue

            links = list(self.articles.loc[self.articles.label == cluster]['id'].dropna())
            unique_sources = set(self.articles.loc[self.articles.label == cluster]['source'])
            if len(unique_sources) < len(links) * 0.6:
                continue


            text = output[output['label'] == cluster]['text'].iloc[0]
            summarizer = LsaSummarizer()
            summarizer.stop_words = _stopwords
            summary = summarizer(text)

            tf_idf_vectorizor_sentences = TfidfVectorizer()
            tf_idf_sentences = tf_idf_vectorizor_sentences.fit_transform(summary)

            counter = 0
            prev_vecs = []
            summarized = []
            for i, sent in enumerate(summary):
                if counter == num_sentences or counter == len(summary):
                    break
                is_similar = False
                for prev_vec in prev_vecs:
                    sim = cosine_similarity(tf_idf_sentences[i], prev_vec)[0, 0]
                    if sim > 0.8:
                        is_similar = True
                        break
                if not is_similar:
                    if len(sent) < 50 or len(sent) > 500:
                        continue
                    summarized.append(sent)
                    prev_vecs.append(tf_idf_sentences[i])
                    counter += 1

            titles = list(self.articles.loc[self.articles.label == cluster]['title'])
            title = max(set(titles), key=titles.count)

            pic = self.articles.loc[self.articles.label == cluster]['photoUrl'].dropna()

            if len(pic) > 0:
                pic = pic.iloc[0]
            else:
                pic = None

            summaries.append(
                {'summary': summarized, 'title': title, 'photoUrl': pic, 'linkedNewsItemIds': links})

        return summaries

    def send_news(self, arcs):
        url = "http://webapp:80/internal/news"
        headers = {'Content-type': 'application/json'}
        r = requests.post(url, data=json.dumps(arcs), headers=headers)
        data = r.json()
        self.articles['id'] = data

    def send_summaries(self, results):
        url = "http://webapp:80/internal/summaries"
        headers = {'Content-type': 'application/json'}
        r = requests.post(url, data=json.dumps(results), headers=headers)
        print(r.status_code)
