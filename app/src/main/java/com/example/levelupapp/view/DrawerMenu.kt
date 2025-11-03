package com.example.levelupapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.ui.theme.*

@Composable
fun DrawerMenu(
    userName: String,
    isAdmin: Boolean,
    isDuocUser: Boolean = false,
    categorias: List<Categoria> = emptyList(),
    featuredProducts: List<Product> = emptyList(),
    onItemSelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showCategorias by remember { mutableStateOf(false) }
    var showFeatured by remember { mutableStateOf(false) }

    Surface(
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
        color = SurfaceColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(270.dp)
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
                text = "Hola, $userName",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(20.dp))
            Divider(color = BlueSecondary.copy(alpha = 0.3f))
            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategorias = !showCategorias }
            ) {
                Text(
                    "Categorías",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (showCategorias) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = BluePrimary
                )
            }

            if (showCategorias) {
                categorias.forEach { categoria ->
                    Text(
                        text = "• ${categoria.nombre}",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 4.dp)
                            .clickable {
                                onItemSelected("category_${categoria.id}")
                            }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Divider(color = BlueSecondary.copy(alpha = 0.3f))
            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showFeatured = !showFeatured }
            ) {
                Text(
                    "Productos destacados",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (showFeatured) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = BluePrimary
                )
            }

            if (showFeatured) {
                featuredProducts.take(5).forEach { producto ->
                    Text(
                        text = "• ${producto.nombre}",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 4.dp)
                            .clickable { onItemSelected("product_${producto.id}") }
                    )
                }

                if (featuredProducts.size > 5) {
                    Text(
                        text = "Ver todos...",
                        color = BluePrimary,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 6.dp)
                            .clickable { onItemSelected("featured_all") }
                    )
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
