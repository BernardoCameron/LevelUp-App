package com.example.levelupapp.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupapp.ui.login.LoginScreen
import com.example.levelupapp.viewmodel.LoginViewModel
import com.example.levelupapp.ui.login.RegisterScreen
import com.example.levelupapp.ui.main.MainScreen
import com.example.levelupapp.view.DrawerMenu
import com.example.levelupapp.view.ProductoFormScreen
import com.example.levelupapp.viewmodel.MainViewModel
import com.example.levelupapp.ui.admin.AdminScreen

@Composable
fun AppNav() {
    val mainViewModel: MainViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }


        composable("main"){
            MainScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable("admin"){
            AdminScreen(navController = navController)
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
