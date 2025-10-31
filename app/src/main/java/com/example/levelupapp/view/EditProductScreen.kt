package com.example.levelupapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    product: Product,
    viewModel: AdminViewModel
) {
    var nombre by remember { mutableStateOf(product.nombre) }
    var descripcion by remember { mutableStateOf(product.descripcion) }
    var precio by remember { mutableStateOf(product.precio.toString()) }
    var imagen by remember { mutableStateOf(product.imagen) }
    var destacado by remember { mutableStateOf(product.destacado) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imagen,
                onValueChange = { imagen = it },
                label = { Text("Imagen (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = destacado,
                    onCheckedChange = { destacado = it }
                )
                Text("Destacado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val updatedProduct = product.copy(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toDoubleOrNull() ?: product.precio,
                        imagen = if (imagen.isNotEmpty()) imagen else product.imagen,
                        destacado = destacado
                    )
                    viewModel.updateProduct(updatedProduct)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
