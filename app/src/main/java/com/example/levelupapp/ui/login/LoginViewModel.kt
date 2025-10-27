package com.example.levelupapp.ui.login

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room

import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: AuthRepository
    var uiState = mutableStateOf(LoginUiState())
        private set

    init {
        val db = Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "levelup_db"
        ).build()

        repo = AuthRepository(db.credentialDao())
    }


    fun onUsernameChange(value: String) {
        uiState.value = uiState.value.copy(username = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState.value = uiState.value.copy(password = value, error = null)
    }

    fun login(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true, error = null)
            val ok = repo.login(uiState.value.username.trim(), uiState.value.password)
            uiState.value = uiState.value.copy(isLoading = false)

            if (ok) onSuccess(uiState.value.username.trim())
            else uiState.value = uiState.value.copy(error = "Credenciales inválidas")
        }
    }
}
