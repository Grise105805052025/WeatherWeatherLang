package klo0812.mlaserna.weatherweatherlang.models.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import klo0812.mlaserna.weatherweatherlang.app.WWLApplication
import klo0812.mlaserna.weatherweatherlang.composables.controllers.MainScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class LoginViewModel(
    private val saveStateHandle: SavedStateHandle
): ViewModel() {

    companion object {
        val TAG: String = LoginViewModel::class.java.simpleName

        fun Factory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LoginViewModel(createSavedStateHandle())
            }
        }

        const val EMAIL_KEY = "email"
        const val PWD_KEY = "password"
        const val ALLOWED_LOGIN = "allowedToLogin"
        const val LOGGING_IN = "loggingIn"
    }

    private val _email = MutableStateFlow(
        saveStateHandle.get<String>(EMAIL_KEY) ?: ""
    )
    val email: StateFlow<String> = _email.asStateFlow()
    fun updateEmail(email: String) {
        _email.value = email
        saveStateHandle[EMAIL_KEY] = email
        updateAllowedToLogin(isAllowedToLogin())
    }

    private val _password = MutableStateFlow(
        saveStateHandle.get<String>(PWD_KEY) ?: ""
    )
    val password: StateFlow<String> = _password.asStateFlow()
    fun updatePassword(password: String) {
        _password.value = password
        saveStateHandle[PWD_KEY] = password
        updateAllowedToLogin(isAllowedToLogin())
    }

    val _allowedToLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val allowedToLogin: StateFlow<Boolean> = _allowedToLogin.asStateFlow()
    fun updateAllowedToLogin(allowedToLogin: Boolean) {
        _allowedToLogin.value = allowedToLogin
        saveStateHandle[ALLOWED_LOGIN] = allowedToLogin
    }

    fun isAllowedToLogin() : Boolean {
        return email.value.isNotEmpty() && password.value.isNotEmpty()
    }

    val _loggingIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loggingIn: StateFlow<Boolean> = _loggingIn.asStateFlow()
    fun updateLoggingIn(loggingIn: Boolean) {
        _loggingIn.value = loggingIn
        saveStateHandle[LOGGING_IN] = loggingIn
    }

    private val lock = Object()
    fun loginUserWithEmail(
        context: Context,
        mainNavController: NavController
    ) {
        synchronized(lock) {
            updateLoggingIn(true)
            // Firebase authentication hashes user password during request so we don't need to
            // implement our own one-way hashing algorithm
            WWLApplication.getFirebaseAuth().signInWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Successfully logged in!", Toast.LENGTH_SHORT).show()
                        mainNavController.navigate(MainScreens.DashboardPage.route) {
                            popUpTo(MainScreens.LoginPage.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        Log.w(TAG, "Failed to login due to: ", task.exception)
                        Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        updateLoggingIn(false)
                    }
                }
        }
    }

}