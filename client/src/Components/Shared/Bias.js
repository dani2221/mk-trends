import React from 'react'
import { Tooltip } from '@mui/material';

const Bias = ({biases, showText}) => {
    return(
        <div style={{width:'100%', textAlign:'center'}}>
            <div style={{display:'inline-block', width: '100%', verticalAlign: 'middle'}}>
                <div style={{width: '100%', height:'5px', display:'flex', borderRadius:'5px'}}>
                <Tooltip title={'Пристрасно кон СДСМ '+(Math.round(biases[0] * 10000) / 100).toFixed(2)+'%'} placement="top">
                    <div style={{flex:biases[0], height:'100%', backgroundColor:'#93CAED'}}>&nbsp;</div>
                </Tooltip>
                <Tooltip title={'Неутрално '+(Math.round(biases[1] * 10000) / 100).toFixed(2)+'%'} placement="top">
                    <div style={{flex:biases[1], height:'100%', backgroundColor:'#F5F5DC'}}>&nbsp;</div>
                </Tooltip>
                <Tooltip title={'Пристрасно кон ВМРО '+(Math.round(biases[2] * 10000) / 100).toFixed(2)+'%'} placement="top">
                    <div style={{flex:biases[2], height:'100%', backgroundColor:'#F47174'}}>&nbsp;</div>
                </Tooltip>
                </div>
            </div>
            {showText ?
            <div style={{ display: 'inline-block', fontSize:'0.875rem', verticalAlign: 'middle', marginLeft:'5px'}}>
                {biases.indexOf(Math.max(...biases)) == 1 ? 'Неутрално '+(Math.round(biases[1] * 10000) / 100).toFixed(2)+'%'
                    : biases.indexOf(Math.max(...biases)) == 0 ? 'Пристрасно кон СДСМ '+(Math.round(biases[0] * 10000) / 100).toFixed(2)+'%'
                        : 'Пристрасно кон ВМРО '+(Math.round(biases[2] * 10000) / 100).toFixed(2)+'%'
                }
            </div>
            : <></>}
        </div>
    )
}

export default Bias;