import './App.css';
import Grid from '@mui/material/Grid';
import { CircularProgress, Paper, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import axios from 'axios';
import NewsCard from './Components/NewsCard';
import process from "process";
import { Masonry } from '@mui/lab';
import logo from './Images/chitaj_logo_dark_long.png'

function App() {

  const [news, setNews] = useState({ hasMore: '', items: [] })
  const [loading, setLoading] = useState(true);
  const [loadingMessage, setLoadingMessage] = useState("Се вчитуваат содржини")

  const development = !process.env.NODE_ENV || process.env.NODE_ENV === 'development';

  let url = ''
  if (false) {
    url = 'http://localhost:8080/?page=0&size=100'
  } else {
    url = 'https://mktrends.azurewebsites.net/?page=0&size=100'
  }

  useEffect(() => {
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
      <Paper style={{ width: '100%', padding: '3px 0', marginBottom: '20px', backgroundColor:'#253439' }}>
        <img src={logo} alt='logo' height={'80px'}></img>
      </Paper>
      <div style={{ margin: '10px' }}>
        <Masonry spacing={3} columns={{ xs: 1, sm: 2, md: 2, lg: 3, xl: 4 }} style={{ margin: 0, width: '100%' }}>
          {loading ?
            <Paper elevation={1} style={{ textAlign: 'center', padding: '30px' }}>
              <CircularProgress size={100} thickness={5} style={{ marginBottom: '20px' }} />
              <br />
              <Typography variant={'p'} color={'white'}>{loadingMessage}</Typography>
            </Paper> :
            <></>
          }
          {news.items.map(el =>
            <NewsCard news={el} key={el.title}></NewsCard>
            )}
        </Masonry>
      </div>
    </div>
  );
}
export default App;