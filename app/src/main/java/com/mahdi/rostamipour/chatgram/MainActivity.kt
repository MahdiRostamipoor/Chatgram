package com.mahdi.rostamipour.chatgram

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.presenter.screen.ChatsListScreen
import com.mahdi.rostamipour.chatgram.presenter.screen.RegisterScreen
import com.mahdi.rostamipour.chatgram.presenter.screen.SplashScreen
import com.mahdi.rostamipour.chatgram.presenter.viewModel.SocketViewModel
import com.mahdi.rostamipour.chatgram.ui.theme.ChatgramTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val socketViewModel : SocketViewModel by viewModel()

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == "USERID") {
            val updatedUserId = MyPreferences.userId
            if (updatedUserId != null){
                socketViewModel.connectSocket(MyPreferences.userId?:0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatgramTheme {
                MainChatgram()
            }
        }

        MyPreferences.registerListener(listener)

    }

    override fun onDestroy() {
        super.onDestroy()
        MyPreferences.unregisterListener(listener)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainChatgram() {

    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold (modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentBackStackEntry?.destination?.route == "ChatsListScreen"){
                TopAppBar(title = {
                    Row(Modifier.fillMaxWidth().fillMaxHeight(), horizontalArrangement = Arrangement.SpaceBetween
                        , verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {

                        }) { Icon(Icons.Default.Menu , contentDescription = "" , tint = Color.White)}

                        Text("Chatgram" , style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White , fontSize = 16.sp)
                        IconButton(onClick = {

                        }) { Icon(Icons.Default.Search , contentDescription = "" , tint = Color.White ,
                            modifier = Modifier.wrapContentWidth(Alignment.End))}
                    }
                } , Modifier.padding(0.dp))
            }
        }) {  innerPadding ->
        NavHost(navController = navController, startDestination = "SplashScreen" , modifier = Modifier.padding(innerPadding)){
            composable("SplashScreen") {
                SplashScreen(navController)
            }

            composable("RegisterScreen") {
                RegisterScreen(navController)
            }

            composable("ChatsListScreen") {
                ChatsListScreen(navController)
            }
        }
    }

}