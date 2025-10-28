package com.example.levelupapp.ui.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupapp.R

data class Product(
    val id: Int,
    val nombre: String,
    val precio: String,
    val imagen: Int
)

@Composable
fun MainScreen(userName: String,navController: NavController) {
    val destacados = listOf(
        Product(1, "Auriculares", "$29.990", R.drawable.headphones),
        Product(2, "Teclado Mecánico", "$45.990", R.drawable.keyboard),
        Product(3, "Mouse Inalámbrico", "$15.990", R.drawable.mouse)
    )

    val productos = listOf(
        Product(4, "Monitor 24''", "$119.990", R.drawable.monitor),
        Product(5, "Tablet Lenovo", "$229.990", R.drawable.tablet),
        Product(6, "Notebook HP", "$499.990", R.drawable.notebook),
        Product(7, "Auriculares", "$29.990", R.drawable.headphones),
        Product(8, "Parlante JBL", "$89.990", R.drawable.microfono)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Bienvenida
        Text(
            text = "Bienvenido, $userName 👋",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        //logo + text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_lvlup),
                contentDescription = "Logo LevelUpApp",
                modifier = Modifier.size(100.dp)
            )
            Text(
                "Lo mejor en tecnología\nsolo acá.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // carrusel
        Text("Destacados", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(destacados) { producto ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(160.dp)
                        .height(180.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Image(
                            painter = painterResource(id = producto.imagen),
                            contentDescription = producto.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = producto.nombre,
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // productos recomendados
        Text("Productos recomendados", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(productos) { producto ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = producto.imagen),
                            contentDescription = producto.nombre,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(producto.nombre, fontWeight = FontWeight.Bold)
                        Text(producto.precio, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(onClick = { /* TODO: acción */ }) {
                            Text("Comprar")
                        }
                    }
                }
            }
        }
    }
}
