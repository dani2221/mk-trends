import { Card, CardContent, Typography } from '@mui/material';
import React, { useEffect } from 'react'
import Bias from '../Shared/Bias';

const LinkedNewsCard = ({ title, source, bias, link }) => {

  const openUrl = (link) => {
    window.open(link, '_blank');
  }

  return (
    <Card onClick={()=>openUrl(link)} style={{borderRadius: 0, margin:'2px', cursor: 'pointer'}} raised={true}>
      <CardContent>
        <div>
          <div style={{ display: 'inline-block', verticalAlign:'middle'}}>
            <Typography sx={{ fontSize: 10 }} gutterBottom>
              {source}
            </Typography>
          </div>
          <div style={{ display: 'inline-block', width:'100px', marginLeft:'5px' }}>
            <Bias biases={bias} showText={false} />
          </div>
        </div>
        <Typography sx={{ fontSize: 12 }} component="div">
          {title}
        </Typography>

      </CardContent>
    </Card>
  )
}

export default LinkedNewsCard;