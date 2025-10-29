package com.example.levelupapp.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import com.example.levelupapp.R
import com.example.levelupapp.view.DrawerMenu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    val userName by mainViewModel.userName.collectAsState()
    val destacados by mainViewModel.featuredProducts.collectAsState()
    val productos by mainViewModel.recommendedProducts.collectAsState()
    val userEmail by mainViewModel.userEmail.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                userName = userName,
                isAdmin = userEmail.endsWith("@levelup.com"),
                featuredProducts = destacados,
                onItemSelected = { }, // falta implementar
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("LevelUp") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // content ppal
                Text(
                    text = "Bienvenido, $userName 👋",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                Text("Destacados", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(destacados) { producto ->
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .height(180.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                val imageResId = getImageResId(producto.imagen)
                                Image(
                                    painter = painterResource(id = imageResId),
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
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Productos recomendados", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productos) { producto ->
                        val imageResId = getImageResId(producto.imagen)
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(3.dp),
                            modifier = Modifier.height(200.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = imageResId),
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
                                Button(onClick = { /* acción futura */ }) {
                                    Text("Comprar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getImageResId(imageName: String): Int {
    val context = LocalContext.current
    return remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}
