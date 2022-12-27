import React from 'react';
import { Masonry } from '@mui/lab';
import NewsCard from './NewsCard';

const FullView = ({news}) => {
    return(
        <Masonry spacing={3} columns={{ xs: 1, sm: 2, md: 2, lg: 3, xl: 4 }} style={{ margin: 0, width: '100%' }}>
          {news.items.map(el =>
            <NewsCard news={el} key={el.title}></NewsCard>
            )}
        </Masonry>
    )
}

export default FullView