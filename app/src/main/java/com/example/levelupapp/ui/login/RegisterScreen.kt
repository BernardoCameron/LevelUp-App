package com.example.levelupapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = AppDatabase.getDatabase(context)
    val repo = AuthRepository(db.credentialDao())

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass1 by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Cuenta") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro de Usuario", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pass1,
                onValueChange = { pass1 = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pass2,
                onValueChange = { pass2 = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (error != null) {
                Text(error ?: "", color = MaterialTheme.colorScheme.error)
            }

            if (success) {
                Text("Usuario registrado correctamente ✅")
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    error = null
                    success = false

                    when {
                        name.isBlank() || email.isBlank() || pass1.isBlank() || pass2.isBlank() ->
                            error = "Todos los campos son obligatorios"
                        pass1 != pass2 ->
                            error = "Las contraseñas no coinciden"
                        else -> {
                            scope.launch {
                                val ok = repo.register(name, email, pass1)
                                if (ok) {
                                    success = true
                                    navController.popBackStack() // volver al login
                                } else {
                                    error = "Ya existe un usuario con ese correo"
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Registrar")
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Volver al inicio de sesión")
            }
        }
    }
}
