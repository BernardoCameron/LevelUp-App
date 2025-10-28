package com.example.levelupapp.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupapp.ui.login.LoginScreen
import com.example.levelupapp.ui.login.LoginViewModel
import com.example.levelupapp.ui.login.RegisterScreen
import com.example.levelupapp.ui.main.MainScreen
import com.example.levelupapp.view.DrawerMenu
import com.example.levelupapp.view.ProductoFormScreen

@Composable
fun AppNav() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }


        composable(
            route = "drawer/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            MainScreen(userName = username, navController = navController)
        }

        // Formulario de producto
        composable(
            route = "productoForm/{nombre}/{precio}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("precio") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = Uri.decode(backStackEntry.arguments?.getString("nombre") ?: "")
            val precio = backStackEntry.arguments?.getString("precio") ?: ""
            ProductoFormScreen(navController = navController, nombre = nombre, precio = precio)
        }
    }
}
