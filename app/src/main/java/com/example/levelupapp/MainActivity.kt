package com.example.levelupapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.levelupapp.navigation.AppNav
import com.example.levelupapp.ui.theme.LevelUpAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Aplica el tema global de la app
            LevelUpAppTheme {
                // Carga la navegación principal: Login → DrawerMenu → ProductoForm
                AppNav()
            }
        }
    }
}
