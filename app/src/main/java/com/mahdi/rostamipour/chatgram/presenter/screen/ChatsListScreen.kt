package com.mahdi.rostamipour.chatgram.presenter.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mahdi.rostamipour.chatgram.R
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.presenter.viewModel.SocketViewModel
import com.mahdi.rostamipour.chatgram.presenter.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import kotlin.math.max

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ChatsListScreen(navigation : NavHostController , userViewModel: UserViewModel = koinViewModel(),
                    socketViewModel : SocketViewModel = koinViewModel()){

    val context = LocalContext.current


    val usersFromApi by userViewModel.usersList.collectAsState()
    val usersFromSocket by socketViewModel.updateUsers.collectAsState()


    val finalUserList = remember(usersFromApi, usersFromSocket) {
        if (!usersFromSocket.isNullOrEmpty()) usersFromSocket else usersFromApi
    }

    LaunchedEffect(true) {
        userViewModel.fetchUsers(MyPreferences.userId?:0)
    }

    CoroutineScope(Dispatchers.Main).launch {
        socketViewModel.message.collect {
            if (it!=null){
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(finalUserList!!.size){


                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .clickable(true, onClick = {

                            val user = finalUserList[it]
                            val jsonUser = Json.encodeToString(User.serializer(), user)
                            navigation.currentBackStackEntry?.savedStateHandle?.set("user", jsonUser)
                            navigation.navigate("ChatScreen")
                        })) {

                        Spacer(modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (finalUserList[it].isOnline) Color.Green else Color.Gray)
                            .wrapContentWidth(Alignment.Start)
                            .wrapContentHeight(Alignment.Top))

                        Icon(painter = painterResource(R.drawable.chat_logo) , contentDescription = "" , tint = Color.Unspecified,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .size(80.dp))


                        Row(modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(2.dp), verticalArrangement = Arrangement.Center) {
                                Text(finalUserList[it].name, maxLines = 1 ,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White)

                                Text(finalUserList[it].message?:"Start chat with ${finalUserList[it].name}", maxLines = 1,
                                    overflow = TextOverflow.Ellipsis)
                            }

                            Text(finalUserList[it].date?:"7/18/25" , modifier = Modifier
                                .wrapContentWidth(
                                    Alignment.End
                                )
                                .wrapContentHeight(Alignment.Top)
                                .padding(end = 8.dp), fontSize = 12.sp)
                        }

                    }

                    if (it != finalUserList.lastIndex){
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(Color.Black))
                    }

                }

            }
        }
    }

}