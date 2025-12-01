package com.example.levelupapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupapp.viewmodel.AdminViewModel
import com.google.gson.Gson
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    vm: AdminViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        vm.refreshProducts()
    }
    var selectedTab by remember { mutableStateOf("productos") } // estado: productos / usuarios

    val products by vm.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // botones (productos y admin) superiores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectedTab = "productos" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "productos")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text("Productos")
                }

                Button(
                    onClick = { selectedTab = "usuarios" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "usuarios")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text("Usuarios")
                }
            }

            Spacer(Modifier.height(24.dp))

            // contenido de tab seleccionado
            when (selectedTab) {
                "productos" -> ProductosSection(navController, vm)
                "usuarios" -> UsuariosSection()
            }
        }
    }
}

@Composable
private fun ProductosSection(navController: NavController, vm: AdminViewModel) {
    val products by vm.products.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { navController.navigate("add_product") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar producto")
        }

        Spacer(Modifier.height(16.dp))

        Text("Productos registrados", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (products.isEmpty()) {
            Text("No hay productos registrados.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = "$${"%,.0f".format(producto.precio)}",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row {
                                IconButton(onClick = { vm.deleteProduct(producto.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                                IconButton(onClick = {
                                    val productJson = URLEncoder.encode(Gson().toJson(producto), "UTF-8")
                                    navController.navigate("editarProducto/$productJson")
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
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
private fun UsuariosSection() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Gestión de usuarios próximamente", style = MaterialTheme.typography.titleMedium)
    }
}
