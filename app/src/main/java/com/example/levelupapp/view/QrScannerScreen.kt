package com.example.levelupapp.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.levelupapp.utils.QrScanner

@Composable
fun QrScannerScreen(
    onQrDetected: (String) -> Unit,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val context = LocalContext.current
    var qrResult by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when {
            !hasCameraPermission -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(24.dp)
                ) {
                    Text(
                        "Permiso de cámara requerido",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onRequestPermission) {
                        Text("Conceder permiso")
                    }
                }
            }

            qrResult == null -> {
                QrScanner(
                    onQrCodeScanned = { qrContent ->
                        qrResult = qrContent
                        Toast.makeText(context, "Codigo detectado ✅", Toast.LENGTH_SHORT).show()
                        onQrDetected(qrContent)
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .height(560.dp)
                        .border(
                            width = 3.dp,
                            color = Color(0xFF2196F3),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .align(Alignment.Center)
                )



                Text(
                    text = "Apunta al código y espera la detección\nUsa el dispositivo en posición horizontal",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 60.dp)
                )
            }
        }
    }
}
