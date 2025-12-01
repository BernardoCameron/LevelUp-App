package com.example.levelupapp.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupapp.R
import com.example.levelupapp.viewmodel.MainViewModel
import com.example.levelupapp.view.DrawerMenu
import com.example.levelupapp.data.model.Product
import kotlinx.coroutines.launch
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                mainViewModel.refreshProductos()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val userName by mainViewModel.userName.collectAsState()
    val destacados by mainViewModel.featuredProducts.collectAsState()
    val productos by mainViewModel.recommendedProducts.collectAsState()
    val userEmail by mainViewModel.userEmail.collectAsState()
    val isDuocUser = userEmail.endsWith("@duocuc.cl", ignoreCase = true)
    val categorias by mainViewModel.categorias.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                userName = userName,
                isAdmin = userEmail.endsWith("@levelup.com"),
                isDuocUser = userEmail.endsWith("@duocuc.cl", ignoreCase = true),
                categorias = categorias,
                featuredProducts = destacados,
                onItemSelected = { route ->
                    if (route == "admin") navController.navigate("admin")
                },
                onCategoriaClick = { catId ->
                    mainViewModel.setCategoriaSeleccionada(catId)
                    scope.launch { drawerState.close() }
                },
                onProductoClick = { prodId ->
                    navController.navigate("productDetail/$prodId?isDuoc=${isDuocUser}")
                    scope.launch { drawerState.close() }
                },
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
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Bienvenido, $userName",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (isDuocUser) {
                        Text(
                            text = "Usuario Duoc UC - 20% OFF en productos",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

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
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Destacados", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(destacados) { producto ->
                            ProductoCard(
                                producto = producto,
                                navController = navController,
                                isDuocUser = isDuocUser
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Productos recomendados", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Productos recomendados", style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = { mainViewModel.setCategoriaSeleccionada(null) }) {
                            Text("Ver todos")
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 600.dp, max = 1600.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize(),
                            userScrollEnabled = true
                        ) {
                            items(productos) { producto ->
                                ProductoCard(
                                    producto = producto,
                                    navController = navController,
                                    isDuocUser = isDuocUser
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductoCard(
    producto: Product,
    navController: NavController,
    isDuocUser: Boolean
) {
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate("productDetail/${producto.id}?isDuoc=${isDuocUser}")
            }
            .height(260.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.nombre,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                placeholder = painterResource(id = com.example.levelupapp.R.drawable.logo_lvlup),
                error = painterResource(id = com.example.levelupapp.R.drawable.logo_lvlup)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(producto.nombre, fontWeight = FontWeight.Bold)

            Text(
                text = "$${"%,.0f".format(producto.precio)}",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("productDetail/${producto.id}?isDuoc=${isDuocUser}")
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Ver más")
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
