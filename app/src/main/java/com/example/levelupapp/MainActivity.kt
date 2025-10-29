package com.example.levelupapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.navigation.AppNav
import com.example.levelupapp.ui.theme.LevelUpAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDatabase.getDatabase(applicationContext) //crea la instancia de db
        setContent {
            LevelUpAppTheme {
                AppNav()
            }
        }
    }
}
