import './App.css';
import Grid from '@mui/material/Grid';
import { CircularProgress, Paper, Typography } from "@mui/material";
import { Masonry } from '@mui/lab';
import { useEffect, useState } from "react";
import axios from 'axios';
import NewsCard from './Components/NewsCard';
import process from "process";

function App() {

  const [news, setNews] = useState({ date: '', results: [] })
  const [loading, setLoading] = useState(true);
  const [loadingMessage, setLoadingMessage] = useState("Се вчитуваат содржини")

  useEffect(() => {

    const development = !process.env.NODE_ENV || process.env.NODE_ENV === 'development';

    let url = ''
    if(development){
      url = 'http://localhost:8080/data'
    }else{
      url = 'https://naas-api.azurewebsites.net/data'
    }


    const timeout = setTimeout(() => {
      setLoadingMessage("Пронајдени се нови содржини. Се сумаризираат и се одредува популарноста. Ве молиме почекајте!")
    }, 5000)
    axios.get(url).then(el => {
      setLoading(false)
      setLoadingMessage("");
      clearTimeout(timeout);
      setNews(el.data);
    }).catch(err => {
      console.log(err);
    })
  }, [])

  return (
    <div className="App">
      <Paper elevation={4} style={{ width: '100%', padding: '3px 0', marginBottom: '20px' }}><Typography variant={'h3'} style={{ fontWeight: '700' }}>MK-TRENDS</Typography></Paper>
      <div style={{margin:'10px'}}>
      <Masonry spacing={3} columns={{ xs: 1, sm: 2 , md:2, lg:3, xl:4}} style={{margin:0, width:'100%'}}>
        {loading ?
          <Paper elevation={1} style={{ textAlign: 'center', padding: '30px' }}>
            <CircularProgress size={100} thickness={5} style={{ marginBottom: '20px' }} />
            <br />
            <Typography variant={'p'} color={'white'}>{loadingMessage}</Typography>
          </Paper> :
          <></>
        }
        {/* <Paper elevation={2} style={{ textAlign: 'center', padding: '30px', width: '100%' }}>
          <Typography variant={'p'} color={'white'}>{'mk-trends e алгоритам кои автоматски превзема, групира и сумаризира вести'}</Typography>
          <br />
          <Link href='https://medium.com/@danilo.najkov/using-clustering-and-summarization-algorithms-for-news-aggregation'>Дознај како работи!</Link>
        </Paper> */}
        {news.results.map(el =>
          <NewsCard news={el} id={el.cluster}></NewsCard>
        )}
      </Masonry>
      </div>
    </div>
  );
}
export default App;