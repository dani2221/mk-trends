import React from 'react'
import { Card, CardContent, CardMedia, Chip, Typography, Tabs, Tab } from "@mui/material";
import '../../../App.css'


const NewsCard = ({ news }) => {

    const openUrl = (link) => {
        window.open(link, '_blank');
    }

    return (
        <Card className={'newsCard'} style={{backgroundColor:'#121a1c'}}>
            <div style={{ textAlign: 'center', margin: '2px', fontSize: '14px' }}>
                Популарност: {news.popularity}
            </div>
            {news.photoUrl == null ? <></> :
                <CardMedia
                    component="img"
                    height="140"
                    image={news.photoUrl}
                    alt="picture for the news"
                />
            }
            <CardContent>
                <Typography gutterBottom variant="p" style={{ fontWeight: '600', textAlign: 'start' }} component="div">
                    {news.title}
                </Typography>
                <ul>
                    {news.summary.map(txt =>
                        <li key={txt}>
                            <Typography variant="body2" color="text.secondary" style={{ textAlign: 'start' }}>
                                {txt}
                            </Typography>
                        </li>
                    )}
                </ul>
                <hr />
                <div style={{height: '30px'}}>
                    <Tabs
                        variant="scrollable"
                        scrollButtons="auto"
                        aria-label="scrollable auto tabs example"
                    >
                        {shuffle(news.links).map(link => {
                            const url = link.split('/')[2].split('/')[0];
                            const parts = url.split('.');
                            let lbl = parts[parts.length - 2] + '.' + parts[parts.length - 1];
                            if (url.includes('.com') && url.includes('.mk')) {
                                lbl = parts[parts.length - 3] + '.' + parts[parts.length - 2] + '.' + parts[parts.length - 1];
                            }
                            return <Tab key={link} label={lbl} onClick={() => openUrl(link)} />
                        })}
                    </Tabs>
                </div>

            </CardContent>
        </Card>
    )
}

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

export default NewsCard;