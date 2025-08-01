package com.mahdi.rostamipour.chatgram.presenter.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mahdi.rostamipour.chatgram.R
import com.mahdi.rostamipour.chatgram.ui.theme.DarkBackground
import com.mahdi.rostamipour.chatgram.ui.theme.DarkSurface

@Composable
fun DialogProfile(name : String, bio : String, onDismiss : () -> Unit){

    var isChecked by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = {onDismiss()}) {

        Box(modifier = Modifier.padding(10.dp).background(color = DarkBackground, shape = RoundedCornerShape(16.dp))){

            Column {

                Row(modifier = Modifier.fillMaxWidth().background(DarkSurface).padding(20.dp)) {

                    Icon(painter = painterResource(R.drawable.logo), modifier = Modifier.size(40.dp).clip(
                        CircleShape
                    ), contentDescription = null)

                    Text(name , style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White , fontSize = 14.sp , modifier = Modifier.padding(8.dp))
                }

                Text("Bio" , fontSize = 12.sp , modifier = Modifier.padding(4.dp))

                Text(bio , style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White , fontSize = 14.sp , modifier = Modifier.padding(8.dp))

                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).padding(top = 8.dp, bottom = 8.dp).background(DarkSurface))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(text = "Notification", modifier = Modifier.weight(1f).wrapContentWidth(Alignment.Start))
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }, modifier = Modifier.wrapContentWidth(Alignment.End)
                    )
                }


            }

        }

    }

}