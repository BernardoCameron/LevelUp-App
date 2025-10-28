package com.example.levelupapp.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.example.levelupapp.ui.login.RegisterUiState

class RegisterViewModel(
    private val context: Context
) : ViewModel() {

    private val repo: AuthRepository by lazy {
        val db = AppDatabase.getDatabase(context)
        AuthRepository(db.credentialDao())
    }


    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value, error = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = null)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(confirmPassword = value, error = null)
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val name = uiState.name.trim()
            val email = uiState.email.trim()
            val password = uiState.password
            val confirm = uiState.confirmPassword

            if (name.isBlank() || email.isBlank() || password.isBlank() || confirm.isBlank()) {
                uiState = uiState.copy(error = "Todos los campos son obligatorios")
                return@launch
            }
            if (password != confirm) {
                uiState = uiState.copy(error = "Las contraseñas no coinciden")
                return@launch
            }

            uiState = uiState.copy(isLoading = true, error = null)

            val ok = repo.register(name, email, password)

            uiState = uiState.copy(isLoading = false)

            if (ok) {
                onSuccess()
            } else {
                uiState = uiState.copy(error = "El usuario ya existe o no se pudo registrar")
            }
        }
    }
}
