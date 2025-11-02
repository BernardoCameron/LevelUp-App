package com.example.levelupapp.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.ui.login.LoginScreen
import com.example.levelupapp.viewmodel.LoginViewModel
import com.example.levelupapp.ui.login.RegisterScreen
import com.example.levelupapp.ui.main.MainScreen
import com.example.levelupapp.view.DrawerMenu
import com.example.levelupapp.view.ProductoFormScreen
import com.example.levelupapp.viewmodel.MainViewModel
import com.example.levelupapp.ui.admin.AdminScreen
import com.example.levelupapp.utils.CameraPermissionHelper
import com.example.levelupapp.view.AddProductScreen
import com.example.levelupapp.view.EditProductScreen
import com.example.levelupapp.view.QrScannerScreen
import com.example.levelupapp.viewmodel.AdminViewModel
import com.google.gson.Gson
import java.net.URLDecoder

@Composable
fun AppNav() {
    val mainViewModel: MainViewModel = viewModel()
    val navController = rememberNavController()
    val adminViewModel: AdminViewModel = viewModel()

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
        composable(
            route = "editarProducto/{productoJson}",
            arguments = listOf(navArgument("productoJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val productoJson = backStackEntry.arguments?.getString("productoJson")
            val producto = productoJson?.let {
                val decoded = URLDecoder.decode(it, "UTF-8")
                Gson().fromJson(decoded, Product::class.java)
            }

            producto?.let {
                EditProductScreen(
                    navController = navController,
                    product = it,
                    viewModel = adminViewModel
                )
            }
        }

        composable("add_product") {
            AddProductScreen(navController = navController, viewModel = adminViewModel)
        }

        composable("qrScanner") {
            QrScannerScreen(
                onQrDetected = { codigo ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("codigoBarras", codigo)
                    navController.popBackStack()
                },
                hasCameraPermission = CameraPermissionHelper.hasCameraPermission(LocalContext.current),
                onRequestPermission = {
                }
            )
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
