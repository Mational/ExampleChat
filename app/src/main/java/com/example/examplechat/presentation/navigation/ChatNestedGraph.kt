package com.example.examplechat.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.examplechat.presentation.chatRoomScreen.ChatRoomScreen

fun NavGraphBuilder.chatGraph(navController: NavController) {
    navigation(startDestination = Screens.ChatListScreen.route, route = Screens.ChatRoute.route) {
        composable(Screens.ChatListScreen.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screens.ChatRoomScreen.createRoom("room_01"))
                    }
                ) {
                    Text("room_01")
                }
                Button(
                    onClick = {
                        navController.navigate(Screens.ChatRoomScreen.createRoom("room_02"))
                    }
                ) {
                    Text("room_02")
                }
            }
        }

        composable(
            route = Screens.ChatRoomScreen.route,
            arguments = listOf(navArgument("roomId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            ChatRoomScreen(navController, roomId = roomId ?: "")
        }
    }
}
