package com.mahdi.rostamipour.chatgram.presenter.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mahdi.rostamipour.chatgram.R
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.presenter.viewModel.SocketViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(navigation : NavHostController,socketViewModel : SocketViewModel = koinViewModel()){

    LaunchedEffect(Unit) {
        delay(5000)
        if (MyPreferences.userId != null){
            navigation.navigate("ChatsListScreen")
            socketViewModel.connectSocket(MyPreferences.userId?:0)
        }else{
            navigation.navigate("RegisterScreen")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Icon(painter = painterResource(R.drawable.chat_logo), contentDescription = "", modifier = Modifier.fillMaxWidth().size(200.dp),
            tint = Color.Unspecified)

    }

}