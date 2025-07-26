package com.mahdi.rostamipour.chatgram.presenter.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mahdi.rostamipour.chatgram.R
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.SendMessage
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.presenter.viewModel.MessageViewModel
import com.mahdi.rostamipour.chatgram.presenter.viewModel.SocketViewModel
import com.mahdi.rostamipour.chatgram.ui.theme.DarkSurface
import com.mahdi.rostamipour.chatgram.ui.theme.Divider
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(navigation : NavHostController, user : User, socketViewModel: SocketViewModel = koinViewModel(),
               messageViewModel: MessageViewModel = koinViewModel()) {

    var textMessage by remember { mutableStateOf("") }

    val messageApi by messageViewModel.messages.collectAsState()
    val messageSocket by socketViewModel.message.collectAsState()
    val sendMessage by messageViewModel.stateSendMessage.collectAsState()

    val finalListMessages = remember(messageApi) {
        mutableStateListOf<GetMessage>().apply {
            clear()
            addAll(messageApi.asReversed())
        }
    }

    // اضافه کردن پیام سوکت
    LaunchedEffect(messageSocket) {
        messageSocket?.let {
            if (finalListMessages.none { msg -> msg.id == it.id }) {
                finalListMessages.add(0, it)
            }
        }
    }

    LaunchedEffect(sendMessage) {
        sendMessage?.let {
            if (finalListMessages.none { msg -> msg.id == it.id }) {
                finalListMessages.add(0, it)
            }
        }
    }

    val chatListState = rememberLazyListState()
    LaunchedEffect(finalListMessages.size) {
        chatListState.animateScrollToItem(0)
    }

    LaunchedEffect(true) {
        messageViewModel.getMessages(MyPreferences.userId?:0,user.id)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.background), contentScale = ContentScale.Crop, contentDescription = null)

        Column(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                state = chatListState,
                reverseLayout = true,
                modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(finalListMessages.size){
                    ListMyMessage(finalListMessages[it])
                }
            }

            Row(modifier = Modifier.fillMaxWidth().background(DarkSurface)) {

                OutlinedTextField(value = textMessage , onValueChange = {
                    textMessage = it
                }, placeholder = { Text("Message") }
                    , modifier = Modifier.fillMaxWidth().weight(1f).background(DarkSurface),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = DarkSurface,
                        focusedContainerColor = DarkSurface,
                        focusedBorderColor = DarkSurface,
                        disabledBorderColor = DarkSurface,
                        unfocusedBorderColor = DarkSurface,
                        errorBorderColor = DarkSurface
                    )
                )

                if (textMessage.isNotEmpty()){
                    IconButton(onClick = {
                        val sendMessage = SendMessage(MyPreferences.userId?:0,user.id,textMessage,
                            "text","7/25/2025")

                        messageViewModel.sendMessage(sendMessage)
                        textMessage = ""
                    }){
                        Icon(Icons.Default.Send, tint = Color.White, contentDescription = null,
                            modifier = Modifier.fillMaxHeight().wrapContentHeight(Alignment.CenterVertically))
                    }
                }else{
                    IconButton(onClick = {

                    }){
                        Icon(painter = painterResource(R.drawable.link), tint = Color.White, contentDescription = null,
                            modifier = Modifier.fillMaxHeight().wrapContentHeight(Alignment.CenterVertically))
                    }
                }

            }
        }

    }

}

@Composable
fun ListMyMessage(getMessage: GetMessage){

    val isMyMessage : Boolean = if(getMessage.senderId == MyPreferences.userId) true else false

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMyMessage) Arrangement.Start else Arrangement.End) {

        Card(modifier = Modifier.padding(8.dp), colors = CardDefaults.cardColors(
            if (isMyMessage) Divider else DarkSurface),
            shape = RoundedCornerShape(topEnd = if (isMyMessage) 8.dp else 2.dp ,
                topStart = if (isMyMessage) 2.dp else 8.dp , bottomStart = if (isMyMessage) 2.dp else 8.dp ,
                bottomEnd = if (isMyMessage) 8.dp else 2.dp)) {
            Column {
                Text(getMessage.message, modifier = Modifier.padding(4.dp), color = Color.White)

                Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.Start , verticalAlignment = Alignment.CenterVertically) {
                    if (isMyMessage){
                        Icon(painter = painterResource(if (getMessage.seen == 0) R.drawable.unseen else R.drawable.seen) ,
                            contentDescription = null ,
                            tint = Color.Unspecified)
                    }

                    Text(getMessage.date, modifier = Modifier.padding(4.dp), color = Color.White , fontSize = 8.sp)
                }

            }
        }

    }
}