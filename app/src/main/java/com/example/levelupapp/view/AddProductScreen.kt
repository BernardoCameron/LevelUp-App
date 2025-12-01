package com.example.levelupapp.view

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
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

    var hasCameraPermission by remember {
        mutableStateOf(
            com.example.levelupapp.utils.CameraPermissionHelper.hasCameraPermission(context)
        )
    }

    // 👇 NUEVO: Uri de la imagen seleccionada
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // launcher para galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        // opcional: mostrar la uri en el campo imagen
        imagen = uri?.lastPathSegment ?: ""
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

            // Campo texto + botón para elegir imagen
            OutlinedTextField(
                value = imagen,
                onValueChange = { imagen = it },
                label = { Text("Imagen (URL opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Image, contentDescription = "Elegir imagen")
                Spacer(Modifier.width(8.dp))
                Text(
                    if (selectedImageUri != null) "Cambiar imagen seleccionada"
                    else "Seleccionar imagen desde el teléfono"
                )
            }

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
                        val precioDouble = precio.toDouble()
                        val categoria = categoriaId.toIntOrNull() ?: 1
                        val codigo = codigoBarras.ifEmpty { null }

                        if (selectedImageUri != null) {
                            // 👉 subir imagen a Supabase Storage y luego crear producto
                            viewModel.addProductWithImage(
                                context = context,
                                imageUri = selectedImageUri!!,
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precioDouble,
                                categoriaId = categoria,
                                destacado = destacado,
                                codigoBarras = codigo
                            )
                        } else {
                            // 👉 sin imagen local, usamos lo que haya en el campo imagen (URL) o default
                            viewModel.addProduct(
                                Product(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precioDouble,
                                    imagen = if (imagen.isNotEmpty()) imagen else "default_image",
                                    categoriaId = categoria,
                                    destacado = destacado,
                                    codigoBarras = codigo
                                )
                            )
                        }

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
