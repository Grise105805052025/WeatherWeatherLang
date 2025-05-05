package klo0812.mlaserna.weatherweatherlang.composables.pages.welcome

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import klo0812.mlaserna.weatherweatherlang.composables.components.button_shape
import klo0812.mlaserna.weatherweatherlang.composables.components.field_shape
import klo0812.mlaserna.weatherweatherlang.composables.controllers.WelcomeScreens
import klo0812.mlaserna.weatherweatherlang.models.LoginViewModel

@Composable
fun LoginContent(
    mainNavController: NavController?,
    welcomePageNavController: NavController?
) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory())
    val focusManager = LocalFocusManager.current

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val allowedToLogin by viewModel.allowedToLogin.collectAsState()
    val isLoggingIn by viewModel.loggingIn.collectAsState()

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Planning a trip outside?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Login to check the current forecast",
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = {
                Text("Email Address")
            },
            shape = field_shape,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            onValueChange = {
                viewModel.updateEmail(it)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            label = {
                Text("Password")
            },
            shape = field_shape,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (allowedToLogin) {
                        viewModel.loginUserWithEmail(
                            context,
                            firebaseAuth,
                            mainNavController!!
                        )
                    }
                }
            ),
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            onValueChange = {
                viewModel.updatePassword(it)
            },
            trailingIcon = {
                val description = if (passwordVisible) "Hide password" else "Show password"
                val image =
                    if (passwordVisible) Icons.Outlined.Visibility
                    else Icons.Outlined.VisibilityOff
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = button_shape,
            enabled = allowedToLogin,
            onClick = {
                viewModel.loginUserWithEmail(
                    context,
                    firebaseAuth,
                    mainNavController!!
                )
            }
        ) {
            Surface (color = Color.Transparent) {
                AnimatedVisibility(
                    visible = isLoggingIn,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                }
                AnimatedVisibility(
                    visible = !isLoggingIn,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                ) {
                    Text("Login")
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                modifier = Modifier.weight(1f),
                shape = button_shape,
                onClick = {
                    Toast.makeText(context, "Not implemented yet!", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("Forgot Password?")
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                modifier = Modifier.weight(1f),
                shape = button_shape,
                onClick = {
                    welcomePageNavController!!.navigate(
                        route = WelcomeScreens.RegistrationContent.route
                    )
                }
            ) {
                Text("Register")
            }
        }
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    LoginContent(null, null)
}