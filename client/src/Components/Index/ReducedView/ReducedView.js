import React, { useEffect, useState } from 'react';
import { Stack, Drawer } from '@mui/material';
import ReducedNewsCard from './ReducedNewsCard';
import ExpandedNews from '../../Expanded/ExpandedNews';

const ReducedView = ({ news }) => {

    const [drawerData, setDrawerData] = useState(null)
    const [prevUrl, setPrevUrl] = useState('')

    const openDrawer = (data) => {
        var newurl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?open=true';
        window.history.pushState({ path: newurl }, '', newurl);
        setDrawerData(data)
    }
    
    useEffect(()=>{
        setPrevUrl(window.location.href)
        window.addEventListener('popstate', function(event) {
            setDrawerData(null)
        }, false);
    },[])

    return (
        <Stack spacing={2} style={{ margin: 0, width: '100%' }}>
            {news.items.map(el =>
                <ReducedNewsCard news={el} key={el.title} clickEv={()=>openDrawer(el)}></ReducedNewsCard>
            )}
            <Drawer
                variant="temporary"
                anchor='right'
                open={drawerData!=null}
                onClose={()=>setDrawerData(null)}
                ModalProps={{
                    keepMounted: true, // Better open performance on mobile.
                }}
                sx={{
                    '& .MuiDrawer-paper': { boxSizing: 'border-box', width: '80%', backgroundColor:'#121a1c'},
                }}
            >
                {drawerData ? <ExpandedNews news={drawerData}/> : ''}
            </Drawer>
        </Stack>
    )
}

export default ReducedView;