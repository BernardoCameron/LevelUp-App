package com.example.levelupapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import android.content.Context
import com.example.levelupapp.ui.login.LoginUiState

class LoginViewModel : ViewModel() {

    private val repo: AuthRepository by lazy {
        val db = AppDatabase.instance
        AuthRepository(db.credentialDao())
    }

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = null)
    }

    fun login(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            val user = repo.login(uiState.email.trim(), uiState.password)

            uiState = uiState.copy(isLoading = false)

            if (user != null) {
                onSuccess(user.username)
            } else {
                uiState = uiState.copy(error = "Credenciales inválidas")
            }
        }
    }
}
