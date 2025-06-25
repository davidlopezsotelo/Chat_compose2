package com.dls.chatcompose2.ui.navigation

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dls.chatcompose2.ui.screens.auth.LoginScreen
import com.dls.chatcompose2.ui.screens.auth.RegisterScreen
import com.dls.chatcompose2.ui.screens.auth.UserEditScreen
import com.dls.chatcompose2.ui.screens.auth.UserScreen
import com.dls.chatcompose2.ui.screens.chats.ChatsScreen
import com.dls.chatcompose2.ui.screens.contacts.ContactsScreen
import com.dls.chatcompose2.ui.screens.home.HomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    Log.d("NavGraph", "Inicializando navegación")

    // version con animaciones:

    AnimatedNavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("user") { UserScreen(navController = navController) }
        composable("edit_user") { UserEditScreen(navController = navController) }
        composable("contacts") { ContactsScreen(navController = navController) }
        composable("chats") { ChatsScreen(navController = navController) }
    }



    // version anterior:

//    NavHost(navController = navController, startDestination = Screen.Login.route) {
//        composable(route = Screen.Login.route) {
//            Log.d("NavGraph", "Mostrando LoginScreen")
//            LoginScreen(navController = navController)
//        }
//        composable(route = Screen.Register.route) {
//            Log.d("NavGraph", "Mostrando RegisterScreen")
//            RegisterScreen(navController = navController)
//        }
//        composable(route = Screen.Home.route) {
//            Log.d("NavGraph", "Mostrando HomeScreen")
//            HomeScreen(navController = navController)
//        }
//
//        composable("edit_user") {
//            UserEditScreen(
//                onUserUpdated = { navController.popBackStack() },
//                onCancel = { navController.popBackStack() }
//            )
//        }
//
//    }
}
