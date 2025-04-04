package com.example.examplechat.presentation.chatRoomScreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun TypingIndicatorItem() {
    val bubbleColor = Color(0xFFF3E5F5)
    val borderColor = Color(0xFFCE93D8)
    val dotColor = Color(0xFF4A148C)
    val alignment = Alignment.Start

    val dotCount = 3
    val delays = listOf(0L, 200L, 400L) // opóźnienie startu dla każdej kropki

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = alignment
    ) {
        Surface(
            modifier = Modifier
                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                .border(1.dp, borderColor, shape = RoundedCornerShape(16.dp))
                .widthIn(max = 280.dp),
            shape = RoundedCornerShape(16.dp),
            color = bubbleColor
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(dotCount) { index ->
                    val offsetY = remember { Animatable(0f) }

                    LaunchedEffect(Unit) {
                        delay(delays[index])
                        while (true) {
                            offsetY.animateTo(
                                targetValue = -6f,
                                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                            )
                            offsetY.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
                            )
                            delay(900)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .offset(y = offsetY.value.dp)
                            .background(dotColor, shape = CircleShape)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TypingIndicatorItemPreview() {
    TypingIndicatorItem()
}