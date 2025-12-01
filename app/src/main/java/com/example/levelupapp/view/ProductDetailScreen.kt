package com.example.levelupapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.viewmodel.ProductDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int,
    isDuocUser: Boolean = false,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val product by viewModel.product.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        product?.let { p ->
            ProductDetailContent(product = p, paddingValues = padding, isDuocUser = isDuocUser)
        }
    }
}

@Composable
fun ProductDetailContent(
    product: Product,
    paddingValues: PaddingValues,
    isDuocUser: Boolean
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val imageRes = remember(product.imagen) {
        context.resources.getIdentifier(product.imagen, "drawable", context.packageName)
            .takeIf { it != 0 } ?: com.example.levelupapp.R.drawable.logo_lvlup
    }

    val precioDescuento = product.precio * 0.8

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = product.imagen,
            contentDescription = product.nombre,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            error = painterResource(id = com.example.levelupapp.R.drawable.logo_lvlup),
            placeholder = painterResource(id = com.example.levelupapp.R.drawable.logo_lvlup)
        )

        Spacer(Modifier.height(16.dp))

        Text(product.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        if (isDuocUser) {
            Text(
                "Precio normal: $${"%,.0f".format(product.precio)}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = TextDecoration.LineThrough
                )
            )
            Text(
                "Precio Duoc UC: $${"%,.0f".format(precioDescuento)}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                "Descuento exclusivo para usuarios Duoc UC",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            Text(
                "$${"%,.0f".format(product.precio)}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(product.descripcion, style = MaterialTheme.typography.bodyMedium)
    }
}
