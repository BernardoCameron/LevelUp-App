package com.example.levelupapp.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DrawerMenu(
    username: String,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Categorías (user: $username)",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }

        // Body con items de menú
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                NavigationDrawerItem(
                    label = { Text("Hamburguesa Clásica") },
                    selected = false,
                    onClick = {
                        val nombre = Uri.encode("Hamburguesa Clásica")
                        val precio = "5000"
                        navController.navigate("productoForm/$nombre/$precio")
                    },
                    icon = { Icon(Icons.Default.Fastfood, contentDescription = "Clásica") }
                )
            }

            item {
                NavigationDrawerItem(
                    label = { Text("Hamburguesa BBQ") },
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.LunchDining, contentDescription = "BBQ") }
                )
            }

            item {
                NavigationDrawerItem(
                    label = { Text("Hamburguesa Veggie") },
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Grass, contentDescription = "Veggie") }
                )
            }

            item {
                NavigationDrawerItem(
                    label = { Text("Hamburguesa Picante") },
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = "Picante") }
                )
            }

            item {
                NavigationDrawerItem(
                    label = { Text("Hamburguesa Doble") },
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Star, contentDescription = "Doble") }
                )
            }
        }

        // Footer
        Text(
            text = "@2025 LevelUpApp",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerMenuPreview() {
    val navController = rememberNavController()
    DrawerMenu(username = "Usuario Prueba", navController = navController)
}
