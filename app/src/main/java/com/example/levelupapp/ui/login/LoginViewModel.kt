package com.example.levelupapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import android.content.Context

class LoginViewModel(
    private val context: Context
) : ViewModel() {

    private val repo: AuthRepository by lazy {
        val db = AppDatabase.getDatabase(context)
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

            val ok = repo.login(uiState.email.trim(), uiState.password)

            uiState = uiState.copy(isLoading = false)

            if (ok) {
                onSuccess(uiState.email.trim())
            } else {
                uiState = uiState.copy(error = "Credenciales invalidas")
            }
        }
    }
}
