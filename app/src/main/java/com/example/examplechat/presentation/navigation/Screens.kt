package com.example.examplechat.presentation.navigation

sealed class Screens(val route: String) {
    // Routes for chat
    data object ChatRoute : Screens("ChatRoute")

    data object ChatListScreen : Screens("ChatListRoute")

    data object ChatRoomScreen : Screens("ChatRoomRoute/{roomId}") {
        fun createRoom(roomId: String) = "ChatRoomRoute/$roomId"
    }

}
