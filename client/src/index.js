import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { initializeApp } from "firebase/app";
import { getAnalytics, logEvent } from "firebase/analytics";


const firebaseConfig = {
  apiKey: "AIzaSyDbcanNx2q7jSRB_IzzvJ4Cn6ZmYOnvHME",
  authDomain: "chitaj.firebaseapp.com",
  projectId: "chitaj",
  storageBucket: "chitaj.appspot.com",
  messagingSenderId: "171151478694",
  appId: "1:171151478694:web:f7c27ec1b3757921ad0967",
  measurementId: "G-XRF0FF5DSG"
};

const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <ThemeProvider theme={darkTheme}>
      <App />
    </ThemeProvider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
