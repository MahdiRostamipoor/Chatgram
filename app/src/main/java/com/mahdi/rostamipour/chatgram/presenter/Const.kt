package com.mahdi.rostamipour.chatgram.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

fun getInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when (parts.size) {
        0 -> ""
        1 -> parts[0].take(1).uppercase()
        else -> (parts[0].take(1) + parts[1].take(1)).uppercase()
    }
}


fun getColorFromName(name: String): Color {
    val colors = listOf(
        Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784),
        Color(0xFFFFB74D), Color(0xFFBA68C8), Color(0xFF4DB6AC)
    )
    val index = (name.hashCode().absoluteValue) % colors.size
    return colors[index]
}


@Composable
fun NameAvatar(name: String, modifier: Modifier = Modifier , size: Dp = 48.dp) {
    val initials = getInitials(name)
    val bgColor = getColorFromName(name)

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(size)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = size.value.times(0.4).sp
        )
    }
}