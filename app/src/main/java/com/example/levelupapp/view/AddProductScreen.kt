package com.example.levelupapp.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.viewmodel.AdminViewModel
import com.example.levelupapp.utils.CameraPermissionHelper
import com.example.levelupapp.utils.QrScanner



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: AdminViewModel
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var destacado by remember { mutableStateOf(false) }
    var categoriaId by remember { mutableStateOf("") }
    var codigoBarras by remember { mutableStateOf("") }

    var showScanner by remember { mutableStateOf(false) }
    var hasCameraPermission by remember {
        mutableStateOf(CameraPermissionHelper.hasCameraPermission(context))
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<String>("codigoBarras")?.observeForever { codigo ->
            codigoBarras = codigo
        }
    }


        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Agregar producto") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = categoriaId,
                    onValueChange = { categoriaId = it },
                    label = { Text("Categoría ID") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("Imagen (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = codigoBarras,
                    onValueChange = { codigoBarras = it },
                    label = { Text("Código de barras / QR") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (!hasCameraPermission)
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        else navController.navigate("qrScanner")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Escanear código")
                    Spacer(Modifier.width(8.dp))
                    Text("Escanear código")
                }


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = destacado, onCheckedChange = { destacado = it })
                    Text("Destacado")
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isNotEmpty() && descripcion.isNotEmpty() && precio.isNotEmpty()) {
                            viewModel.addProduct(
                                Product(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precio.toDouble(),
                                    imagen = if (imagen.isNotEmpty()) imagen else "default_image",
                                    categoriaId = categoriaId.toIntOrNull() ?: 1,
                                    destacado = destacado,
                                    codigoBarras = codigoBarras.ifEmpty { null }
                                )
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar producto")
                }
            }
        }

}
