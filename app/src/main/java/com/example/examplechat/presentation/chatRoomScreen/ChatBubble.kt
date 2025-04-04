package com.example.examplechat.presentation.chatRoomScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.examplechat.domain.model.ChatMessage
import com.example.examplechat.utils.formatMessageTimestamp

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ChatBubble(
    message: ChatMessage,
    isOwnMessage: Boolean,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    val bubbleColor = if (isOwnMessage) Color(0xFFE0F7FA) else Color(0xFFF3E5F5)
    val borderColor = if (isOwnMessage) Color(0xFF81D4FA) else Color(0xFFCE93D8)
    val textColor = if (isOwnMessage) Color(0xFF004D40) else Color(0xFF4A148C)
    val alignment = if (isOwnMessage) Alignment.End else Alignment.Start

    val timestampFormatted = formatMessageTimestamp(message.timestamp) ?: "Brak czasu"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = alignment
    ) {
        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.00f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = timestampFormatted,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.DarkGray
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource()
                ) { onExpandToggle() }
                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                .border(1.dp, borderColor, shape = RoundedCornerShape(16.dp))
                .widthIn(max = 280.dp),
            shape = RoundedCornerShape(16.dp),
            color = bubbleColor
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                )
            }
        }
    }
}