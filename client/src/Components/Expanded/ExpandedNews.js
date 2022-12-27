import React, { useEffect } from 'react';
import { Typography, Tabs, Tab } from '@mui/material';
import '../../App.css'
import Bias from '../Shared/Bias';
import LinkedNewsCard from './LinkedNewsCard';

const ExpandedNews = ({ news }) => {

    function shuffle(array) {
        array = [...new Set(array)];
        let currentIndex = array.length, randomIndex;

        // While there remain elements to shuffle.
        while (currentIndex != 0) {

            // Pick a remaining element.
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex--;

            // And swap it with the current element.
            [array[currentIndex], array[randomIndex]] = [
                array[randomIndex], array[currentIndex]];
        }

        return array;
    }
    return (
        <div style={{ padding: '10px' }}>
            <div id='expandedHeader'>
                <div id='expandedText'>
                    <Typography id={'expandedTitle'} gutterBottom style={{ fontWeight: '600', textAlign: 'start' }} component="div">
                        {news.title}
                    </Typography>
                    <div style={{ display: 'block' }}>
                        <Bias width={'70%'} biases={news.averageBias} showText={true} />
                    </div>
                    <div>
                        <ul>
                            {news.summary.map(txt =>
                                <li key={txt}>
                                    <Typography variant="body2" color="text.secondary" style={{ textAlign: 'start' }}>
                                        {txt}
                                    </Typography>
                                </li>
                            )}
                        </ul>
                    </div>
                </div>
                <div style={{ flex: 0.1 }}></div>
                <div id='expandedImage'>
                    {news.photoUrl == null ? <></> :
                        <img
                            id={'expandedImage'}
                            src={news.photoUrl}
                            alt="picture for the news"
                            style={{ objectFit: 'cover', width: '100%', borderRadius: '10px' }}
                        />
                    }
                </div>
            </div>
            <Typography variant="h6" style={{ textAlign: 'start' }}>
                Објави од портали
            </Typography>
            <div style={{ height: '30px' }}>
                {news.linkedNews.map(linked => {
                    const url = linked.link.split('/')[2].split('/')[0];
                    const parts = url.split('.');
                    let lbl = parts[parts.length - 2] + '.' + parts[parts.length - 1];
                    if (url.includes('.com') && url.includes('.mk')) {
                        lbl = parts[parts.length - 3] + '.' + parts[parts.length - 2] + '.' + parts[parts.length - 1];
                    }
                    return <LinkedNewsCard key={linked.id} title={linked.title} source={lbl} link={linked.link} bias={linked.bias} />
                })}
            </div>
        </div>
    )
}
export default ExpandedNews;