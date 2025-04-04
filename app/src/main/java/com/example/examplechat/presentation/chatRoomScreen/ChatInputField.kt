package com.example.examplechat.presentation.chatRoomScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun ChatInputField(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp), // więcej luzu od krawędzi
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Lekka kapsuła inputa — efekt „pływania”
        Surface(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 38.dp, max = 96.dp)
                .shadow(8.dp, shape = RoundedCornerShape(24.dp)), // silniejszy cień
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp), // mniejsze paddingi
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                maxLines = 3,
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Text(
                            text = "Wpisz wiadomość...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF9E9E9E))
                        )
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Pływający, stylowy przycisk do wysyłania
        Box(
            modifier = Modifier
                .size(52.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Wyślij",
                    tint = if (text.isNotBlank()) Color(0xFF7E57C2) else Color(0xFF90A4AE),
                    modifier = Modifier.size(42.dp)
                )
            }
        }
    }
}