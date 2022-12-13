import json

from flask_cors import CORS

from Model.cluster_summarize import TextClusterSummarize
from flask import Flask, request, jsonify
from datetime import datetime, timedelta

app = Flask(__name__)
results = list()
last_update = datetime.now()


@app.route('/data', methods=['GET'])
def data():
    global results
    global last_update

    file = open('./Model/urls.json', encoding='utf8')
    urls = json.load(file)
    scraper = TextClusterSummarize(urls)
    results = scraper.cluster_summarize(summarize_alg='lst')
    last_update = datetime.now()
    
    final = {'results': results, 'date': last_update.strftime("%HH:%MM %d/%m/%Y")}
    response = jsonify(final)
    response.headers.add('Access-Control-Allow-Origin', '*')
    return response


@app.route('/', methods=['GET'])
def index():
    return 'get out of here'


if __name__ == "__main__":
    is_prod = True
    dev = {'webapp': 'http://localhost:80'}
    prod = {'webapp': 'http://webapp:80'}
    active = prod if is_prod else dev

    app.config['JSON_AS_ASCII'] = False
    app.config['CORS_HEADERS'] = 'Content-Type'
    CORS(app)
    if is_prod:
        app.run()
    else:
        app.run(port=8081)