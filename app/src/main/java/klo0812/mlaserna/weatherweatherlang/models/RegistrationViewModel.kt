package klo0812.mlaserna.weatherweatherlang.models

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
import com.google.firebase.auth.FirebaseAuth
import klo0812.mlaserna.weatherweatherlang.composables.controllers.MainScreens
import klo0812.mlaserna.weatherweatherlang.composables.pages.welcome.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistrationViewModel(
    private val saveStateHandle: SavedStateHandle
): ViewModel() {

    companion object {
        fun Factory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RegistrationViewModel(createSavedStateHandle())
            }
        }

        const val PWD_REGEX = "^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>])(?=.*[0-9]).{8,}\$"
        const val EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        
        const val EMAIL_KEY = "email"
        const val PWD_KEY = "password"
        const val CON_PWD_KEY = "confirmPassword"
        const val ALLOWED_REGISTER = "allowedToRegister"
        const val REGISTERING = "registering"
    }
    
    private val _email = MutableStateFlow(
        saveStateHandle.get<String>(EMAIL_KEY) ?: ""
    )
    val email: StateFlow<String> = _email.asStateFlow()
    private val _emailError = MutableStateFlow(false)
    val emailError: StateFlow<Boolean> = _emailError.asStateFlow()
    fun updateEmail(email: String) {
        _email.value = email
        saveStateHandle[EMAIL_KEY] = email
        _emailError.update {
            !validateEmail()
        }
        _allowedToRegister.update {
            isAllowedToRegister()
        }
    }

    fun validateEmail(): Boolean {
        val email = email.value
        return EMAIL_REGEX.toRegex().matches(email)
    }
    
    private val _password = MutableStateFlow(
        saveStateHandle.get<String>(PWD_KEY) ?: ""
    )
    val password: StateFlow<String> = _password.asStateFlow()
    val _passwordError: MutableStateFlow<Array<Boolean>> = MutableStateFlow(arrayOf(false, false, false, false))
    val passwordError: StateFlow<Array<Boolean>> = _passwordError.asStateFlow()
    fun updatePassword(password: String) {
        _password.value = password
        saveStateHandle[PWD_KEY] = password
        _passwordError.update {
            validatePassword()
        }
        updateAllowToRegister(isAllowedToRegister())
    }

    fun validatePassword() : Array<Boolean> {
        return if (password.value.isEmpty()) {
            arrayOf(false, false, false, false)
        } else {
            val password = password.value
            var errors = arrayOf(false, false, false, false)
            if (!PWD_REGEX.toRegex().matches(password)) {
                if (password.length < 8) {
                    errors[0] = true
                }
                if (!password.any { it.isUpperCase() }) {
                    errors[1] = true
                }
                if (!password.any { it.isDigit() }) {
                    errors[2] = true
                }
                if (!password.any { it in "!@#\$%^&*(),.?\":{}|<>" }) {
                    errors[3] = true
                }
            }
            return errors
        }
    }
    
    private val _confirmPassword = MutableStateFlow(
        saveStateHandle.get<String>(CON_PWD_KEY) ?: ""
    )
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()
    val _confirmPasswordError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val confirmPasswordError: StateFlow<Boolean> = _confirmPasswordError.asStateFlow()
    fun updateConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        saveStateHandle[CON_PWD_KEY] = confirmPassword
        _confirmPasswordError.update {
            !passwordsMatch()
        }
        updateAllowToRegister(isAllowedToRegister())
    }

    fun passwordsMatch() : Boolean {
        return password.value == confirmPassword.value
    }

    val _allowedToRegister: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val allowedToRegister: StateFlow<Boolean> = _allowedToRegister.asStateFlow()
    fun updateAllowToRegister(allowToRegister: Boolean) {
        _allowedToRegister.value = allowToRegister
        saveStateHandle[ALLOWED_REGISTER] = allowToRegister
    }

    fun isAllowedToRegister() : Boolean {
        return !emailError.value &&
                passwordError.value.all { !it } &&
                !confirmPasswordError.value
    }

    val _registering: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val registering: StateFlow<Boolean> = _registering.asStateFlow()
    fun updateRegistering(loggingIn: Boolean) {
        _registering.value = loggingIn
        saveStateHandle[REGISTERING] = loggingIn
    }

    private val lock = Object()
    fun registerUserWithEmail(
        context: Context,
        auth: FirebaseAuth,
        mainNavController: NavController
    ) {
        synchronized(lock) {
            updateRegistering(true)
            auth.createUserWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "New user created!")
                        Toast.makeText(context, "Successfully registered!", Toast.LENGTH_SHORT).show()
                        mainNavController.navigate(MainScreens.DashboardPage.route) {
                            popUpTo(MainScreens.LoginPage.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        Log.w(TAG, "Ran into an issue: ", task.exception)
                        Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        updateRegistering(false)
                    }
                }
        }
    }

}