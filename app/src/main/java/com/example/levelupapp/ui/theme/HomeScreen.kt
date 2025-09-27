package com.example.digita.ui.theme


import android.annotation.SuppressLint
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.levelupapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(){
    // Definimos un esquema de colores
    val ColorScheme = darkColorScheme(
        primary =Color(0xff000000),
        onPrimary = Color.White,
        onSurface = Color(0xFF182FB0), // Texto oscuro para el fondo gris
    )  //fin dark

    MaterialTheme(
        colorScheme = ColorScheme
    ){ // inicio Aplicar Material


        Scaffold (
            topBar={
                // Define Contenido barra superior
                TopAppBar(title= { Text("Mi Primer App",
                    color = MaterialTheme.colorScheme.onPrimary,
                )}
                )
            }// fin topbar

        )//fin Scaff
        {  innerPadding ->
            //Inicio innerPadding
            Column (
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color(0xFFF0F0F0)), // Fondo gris claro
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
            ) // fin Columna
            { // Inicio Contenido
                // inicio Text
                Text(text="Bienvenido",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Image(
                    painter = painterResource(id= R.drawable.logoduoc),
                    contentDescription ="Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    contentScale = ContentScale.Fit
                )// fin Image

                // Agregar un espacio de 16.dp entre la imagen y el botón
                Spacer(modifier = Modifier.height(66.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), // Añade espacio solo en los márgenes de la pantalla
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        "Texto uno",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), // Color de texto más suave
                            fontWeight = FontWeight.Bold // Texto en negrita
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp) // Añade espacio entre el texto y el borde
                    )

                    // Texto dos con estilo atractivo y color amigable
                    Text(
                        "Texto dos",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), // Color de texto más suave
                            fontWeight = FontWeight.Bold // Texto en negrita
                        ),
                        modifier = Modifier
                            .padding(start = 18.dp) // Añade espacio entre el texto y el borde
                    )
                }

                // Agregar un espacio de 16.dp entre la imagen y el botón
                Spacer(modifier = Modifier.height(66.dp))

                Button(onClick ={/* accion futura*/},
                    //modifier = Modifier.fillMaxWidth()
                    modifier = Modifier.width(200.dp) // Ajusta el valor según lo que necesites

                ) {
                    Text("Presioname")
                }// fin Button

            } // fin Contenido

        }//Fin innerPadding

    } // fin Aplicar Material

}// fin HomeScreen


@Preview(showBackground = true)
@Composable

fun HomeScreenPreview(){
    HomeScreen()
}
