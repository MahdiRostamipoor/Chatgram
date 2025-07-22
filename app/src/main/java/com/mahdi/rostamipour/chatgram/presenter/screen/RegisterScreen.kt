package com.mahdi.rostamipour.chatgram.presenter.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.mahdi.rostamipour.chatgram.R
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.presenter.viewModel.UserViewModel
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen( navigation : NavHostController,
    userViewModel : UserViewModel = koinViewModel()) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    val userIdState by userViewModel.userID.collectAsState()

    if (userIdState != null){

        val json = JSONObject(userIdState.toString())
        if (json.has("id")){
            val userId = json.getInt("id")
            MyPreferences.userId = userId
            navigation.navigate("ChatsListScreen")
            Toast.makeText(context, "Welcome to Chatgram(;", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "problem occurred", Toast.LENGTH_SHORT).show()
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Icon(painter = painterResource(R.drawable.chat_logo), contentDescription = "", modifier = Modifier.fillMaxWidth().size(160.dp),
            tint = Color.Unspecified)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = {
            name = it
        }, modifier = Modifier.fillMaxWidth().padding(8.dp), placeholder = {Text("UserName")})

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = bio , onValueChange = {
            bio = it
        }, modifier = Modifier.fillMaxWidth().padding(8.dp), placeholder = {Text("Bio")})
        Spacer(modifier = Modifier.height(28.dp))
        Button(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp), onClick = {
            if (name.isNotBlank()) {
                userViewModel.registerUser(name, if (bio.isEmpty()) "" else bio)
            }else{
                Toast.makeText(context,"Enter your name", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Register")
        }
    }
}