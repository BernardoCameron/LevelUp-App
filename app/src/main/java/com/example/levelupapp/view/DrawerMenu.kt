package com.example.levelupapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.ui.theme.*

@Composable
fun DrawerMenu(
    userName: String,
    isAdmin: Boolean,
    featuredProducts: List<Product> = emptyList(),
    onItemSelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showFeatured by remember { mutableStateOf(false) }

    Surface(
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
        color = SurfaceColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(260.dp)
                .background(SurfaceColor)
                .shadow(
                    elevation = 6.dp,
                    ambientColor = BluePrimary.copy(alpha = 0.3f),
                    spotColor = BluePrimary.copy(alpha = 0.3f)
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Header
            Text(
                text = "Hola, $userName 👋",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(20.dp))
            Divider(color = BlueSecondary.copy(alpha = 0.3f))
            Spacer(Modifier.height(20.dp))

            DrawerOption("Categorías") { onItemSelected("categories") }

            // 🔽 Productos destacados con sublista
            Column {
                DrawerOption(
                    title = "Productos destacados",
                    onClick = { showFeatured = !showFeatured }
                )

                if (showFeatured) {
                    featuredProducts.forEach { producto ->
                        Text(
                            text = "• ${producto.nombre}",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 4.dp)
                                .clickable { onItemSelected("product_${producto.id}") }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Divider(color = BlueSecondary.copy(alpha = 0.3f))
            Spacer(Modifier.height(16.dp))

            DrawerOption("Mi cuenta") { onItemSelected("account") }
            DrawerOption("Cerrar sesión") { onLogout() }

            if (isAdmin) {
                Spacer(Modifier.height(16.dp))
                Divider(color = PurpleAccent.copy(alpha = 0.3f))
                Spacer(Modifier.height(16.dp))
                DrawerOption("Administración", highlight = true) { onItemSelected("admin") }
            }
        }
    }
}

@Composable
private fun DrawerOption(
    title: String,
    highlight: Boolean = false,
    onClick: () -> Unit
) {
    Text(
        text = title,
        color = if (highlight) BluePrimary else TextSecondary,
        fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    )
}
