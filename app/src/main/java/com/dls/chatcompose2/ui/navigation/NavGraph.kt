package com.dls.chatcompose2.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dls.chatcompose2.ui.screens.auth.LoginScreen
import com.dls.chatcompose2.ui.screens.auth.RegisterScreen
import com.dls.chatcompose2.ui.screens.auth.UserEditScreen
import com.dls.chatcompose2.ui.screens.home.HomeScreen


@Composable
fun NavGraph(navController: NavHostController) {
    Log.d("NavGraph", "Inicializando navegación")

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            Log.d("NavGraph", "Mostrando LoginScreen")
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            Log.d("NavGraph", "Mostrando RegisterScreen")
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            Log.d("NavGraph", "Mostrando HomeScreen")
            HomeScreen(navController = navController)
        }

        composable("edit_user") {
            UserEditScreen(
                onUserUpdated = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
