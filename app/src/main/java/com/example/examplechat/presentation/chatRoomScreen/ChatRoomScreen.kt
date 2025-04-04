package com.example.examplechat.presentation.chatRoomScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.examplechat.domain.model.ChatMessage

sealed class ChatItem {
    data class DateSeparator(val formattedDate: String) : ChatItem()
    data class MessageItem(
        val message: ChatMessage,
        val isExpandable: Boolean,
        val isOwnMessage: Boolean
    ) : ChatItem()
    data object TypingIndicator: ChatItem()
    data object LoadingItem : ChatItem()
}

@Composable
fun ChatRoomScreen(
    navController: NavController,
    roomId: String
) {
    val viewModel: ChatRoomViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val itemsLeftToTop by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            uiState.chatItems.size - 1 - lastVisibleIndex
        }
    }


    val previousSize = remember { mutableIntStateOf(0) }

    LaunchedEffect(uiState.chatItems) {
        val currentSize = uiState.chatItems.size

        if (currentSize > previousSize.intValue) {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val firstVisibleOffset = listState.firstVisibleItemScrollOffset
            val addedItems = currentSize - previousSize.intValue

            Log.d("NO_JUMP", "Added $addedItems items, restoring scroll position")

            listState.scrollToItem(
                index = firstVisibleIndex,
                scrollOffset = firstVisibleOffset
            )
        }

        previousSize.value = currentSize
    }

    LaunchedEffect(itemsLeftToTop) {
        if (itemsLeftToTop in 13..100 && !uiState.isLoadingMore && uiState.messages.isNotEmpty() && uiState.canLoadMoreMessages) {
            Log.d("ROOM_MSG", "items_to_top: $itemsLeftToTop")
            Log.d("ROOM_MSG", "should retrieve previous messages")
            viewModel.loadPreviousMessages()
        }
    }

    LaunchedEffect(uiState.shouldScrollToBottom) {
        if (uiState.shouldScrollToBottom) {
            listState.animateScrollToItem(0)
            viewModel.markScrolledBottom()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFEDFDFD),
                        Color(0xFFC1F1F4),
                        Color(0xFF8ADAE6),
                        Color(0xFF5FC8D6),
                        Color(0xFF3BAEBF)
                    ),
                    radius = 1500f
                )
            )
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
            ChatTopBar(
                userName = "Anna Nowak",
                userImageUrl = null,
                onBackClick = { navController.navigateUp() },
                onOptionsClick = { /* TODO: otwórz menu */ }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            if (uiState.messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    reverseLayout = true,
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
//                    Log.d("ROOM_MSG", "size: ${listState.layoutInfo.totalItemsCount}")
                    items(uiState.chatItems) { item ->
                        when (item) {
                            is ChatItem.DateSeparator -> {
                                DateSeparatorItem(item.formattedDate)
                            }
                            is ChatItem.MessageItem -> {
                                ChatBubble(
                                    message = item.message,
                                    isOwnMessage = item.isOwnMessage,
                                    isExpanded = if (item.isExpandable) uiState.expandedMessageId == item.message.serial else false,
                                    onExpandToggle = {
                                        if (item.isExpandable) {
                                            viewModel.onExpandToggle(item.message.serial)
                                        }
                                    }
                                )
                            }
                            is ChatItem.TypingIndicator -> {
                                TypingIndicatorItem()
                            }
                            is ChatItem.LoadingItem -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(28.dp),
                                        color = Color(0XFF1A1A1A)
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = Color.Black)

                ChatInputField(
                    text = uiState.messageText,
                    onTextChange = viewModel::onMessageTextChange,
                    onSend = {
                        viewModel.sendMessage()
                    }
                )
            }
        }
    }
}

