package com.mahdi.rostamipour.chatgram

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.presenter.screen.ChatScreen
import com.mahdi.rostamipour.chatgram.presenter.screen.ChatsListScreen
import com.mahdi.rostamipour.chatgram.presenter.screen.DialogProfile
import com.mahdi.rostamipour.chatgram.presenter.screen.RegisterScreen
import com.mahdi.rostamipour.chatgram.presenter.screen.SplashScreen
import com.mahdi.rostamipour.chatgram.presenter.viewModel.SocketViewModel
import com.mahdi.rostamipour.chatgram.ui.theme.ChatgramTheme
import com.mahdi.rostamipour.chatgram.ui.theme.DarkBackground
import com.mahdi.rostamipour.chatgram.ui.theme.DarkSurface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
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
                MainChatgram(socketViewModel)
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
fun MainChatgram(socketViewModel : SocketViewModel) {

    val context = LocalContext.current

    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var isShowProfileDialog by remember { mutableStateOf(false) }


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val stateTyping by socketViewModel.typing.collectAsState()

    LaunchedEffect(stateTyping) {
        socketViewModel.getTyping()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerShape = RectangleShape,  drawerContainerColor = DarkBackground) {

                Column {

                    Box(modifier = Modifier.weight(0.5f)){
                        Image(painter = painterResource(R.drawable.background), modifier = Modifier.fillMaxWidth()
                            , contentDescription = null , contentScale = ContentScale.Crop)

                        Column(modifier = Modifier.fillMaxHeight().padding(start = 8.dp), verticalArrangement = Arrangement.Center) {
                            Icon(painter = painterResource(R.drawable.logo), modifier = Modifier
                                .size(52.dp)
                                .clip(
                                    CircleShape
                                ), contentDescription = null)

                            Text("Chatgram" , style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White , fontSize = 14.sp , modifier = Modifier.padding(4.dp))
                        }
                    }

                    Column(modifier = Modifier.weight(3f)) {

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(4.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ic_profile), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("My Profile" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(8.dp))

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(0.5.dp).background(
                            DarkSurface
                        ))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ico_group), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("New Group" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(8.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ic_person), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("Contacts" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(8.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ic_call), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("Calls" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(8.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ic_save), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("Saved Message" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(8.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ic_setting), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("Setting" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(8.dp))

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(0.5.dp).background(
                            DarkSurface
                        ))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ic_invite), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("Invite Friends" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                        Spacer(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(8.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Icon(painter = painterResource(R.drawable.ic_about), contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(24.dp))

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("About Chatgram" , color = Color.White,style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp )

                        }

                    }
                }

                /*Text("منوی چتگرام", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)

                NavigationDrawerItem(
                    label = { Text("پروفایل") },
                    selected = false,
                    onClick = {
                        isShowProfileDialog = true
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("تنظیمات") },
                    selected = false,
                    onClick = {
                        // می‌تونی ناوبری کنی مثلاً
                        // navController.navigate("Settings")
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("خروج") },
                    selected = false,
                    onClick = {
                        // لاگ‌اوت یا خروج
                        scope.launch { drawerState.close() }
                    }
                )*/
            }
        }
    ) {
        Scaffold (modifier = Modifier.fillMaxSize(),
            topBar = {
                if (currentBackStackEntry?.destination?.route == "ChatsListScreen"){
                    TopAppBar(title = {
                        Row(Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), horizontalArrangement = Arrangement.SpaceBetween
                            , verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) { Icon(Icons.Default.Menu , contentDescription = "" , tint = Color.White)}

                            Text("Chatgram" , style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White , fontSize = 16.sp)
                            IconButton(onClick = {

                            }) { Icon(Icons.Default.Search , contentDescription = "" , tint = Color.White ,
                                modifier = Modifier.wrapContentWidth(Alignment.End))}
                        }
                    } , Modifier.padding(0.dp))
                }else if (currentBackStackEntry?.destination?.route == "ChatScreen"){
                    TopAppBar(title = {
                        Row(Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), horizontalArrangement = Arrangement.SpaceBetween
                            , verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) { Icon(Icons.Default.ArrowBack , contentDescription = "" , tint = Color.White)}

                            Row(modifier = Modifier
                                .weight(1f)
                                .clickable(true, onClick = {
                                    isShowProfileDialog = true
                                })) {

                                Icon(painter = painterResource(R.drawable.logo), modifier = Modifier
                                    .size(40.dp)
                                    .clip(
                                        CircleShape
                                    ), contentDescription = null)

                                Text("Chatgram" , style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White , fontSize = 14.sp , modifier = Modifier.padding(4.dp))
                            }


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

                composable("ChatScreen") {
                    val jsonUser = navController.previousBackStackEntry?.savedStateHandle?.get<String>("user")
                    val user = jsonUser?.let { Json.decodeFromString(User.serializer(), it) }
                    user?.let { it1 -> ChatScreen(navigation = navController,user = it1) }
                }
            }

            if (isShowProfileDialog) {
                DialogProfile(
                    "Mrp",
                    "My name is Mahdi Rostamipour",
                    onDismiss = {
                        isShowProfileDialog = false
                    })
            }
        }
    }

}