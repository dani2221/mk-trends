import React from 'react';
import { Card, Typography } from "@mui/material";
import '../../../App.css'
import Bias from '../../Shared/Bias';
import moment from 'moment';

const getTimeText = date => {
    const mnt = moment(date).locale('mk')
    moment.updateLocale('mk', {
        relativeTime : {
            future: "in %s",
            past:   "%s ago",
            s  : 'пред неколку секунди',
            ss : 'пред %d секунди',
            m:  "пред минута",
            mm: "пред %d минути",
            h:  "пред еден час",
            hh: "пред %d часа",
            d:  "пред еден ден",
            dd: "пред %d дена",
            M:  "пред еден месец",
            MM: "%d months",
            y:  "a year",
            yy: "%d years"
        }
    });
    return mnt.fromNow(true)

}

const ReducedNewsCard = ({news, clickEv}) => {
    return(
        <Card style={{backgroundColor:'#121a1c', cursor:'pointer'}} id={'reducedCard'} onClick={clickEv}>
            <div style={{display:'flex', height:'100%', flexDirection:'row', padding:'10px', justifyContent:'space-between'}}>
                <div style={{flex:1}}>
                {news.photoUrl == null ? <></> :
                    <img
                        src={news.photoUrl}
                        alt="picture for the news"
                        style={{objectFit:'cover', width:'100%', aspectRatio:1, borderRadius:'10px'}}
                    />
                    
                }
                <div style={{display:'block'}}>
                        <Bias biases={news.averageBias} showText={false}/>
                    </div>
                </div>
                <div style={{flex:0.2}}></div>
                <div id='titleSize'>
                    <Typography gutterBottom variant="p" style={{ fontWeight: '600', textAlign: 'start', flex:2 }} component="div">
                        {news.title.length > 95 && window.innerWidth < 600 ? news.title.substring(0,95) + '...' : news.title}
                    </Typography>
                    <div style={{flex:1}}>
                        <Typography gutterBottom variant="p" style={{textAlign: 'start', fontSize:'13px', color:'#B29E84'}} component="div">
                            Објавено од {news.popularity} портали
                        </Typography>
                        <Typography gutterBottom variant="p" color={'text.secondary'} style={{textAlign: 'start', fontSize:'13px'}} component="div">
                            {getTimeText(news.firstIndexed)}, обновено {news.timesIndexed==1 ? 'еднаш' : news.timesIndexed+' пати'}
                        </Typography>
                    </div>
                </div>
            </div>
        </Card>
    )
}

export default ReducedNewsCard;