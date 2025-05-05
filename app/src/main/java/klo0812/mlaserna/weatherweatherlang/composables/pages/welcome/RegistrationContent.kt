package klo0812.mlaserna.weatherweatherlang.composables.pages.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import klo0812.mlaserna.weatherweatherlang.composables.components.FieldInstructionText
import klo0812.mlaserna.weatherweatherlang.composables.components.button_shape
import klo0812.mlaserna.weatherweatherlang.composables.components.field_shape
import klo0812.mlaserna.weatherweatherlang.composables.controllers.WelcomeScreens
import klo0812.mlaserna.weatherweatherlang.models.RegistrationViewModel

const val TAG = "RegistrationContent"

@Composable
fun RegistrationContent(
    mainNavController: NavController?,
    welcomePageNavController: NavController?
) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val viewModel: RegistrationViewModel = viewModel(factory = RegistrationViewModel.Factory())
    val focusManager = LocalFocusManager.current

    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    val allowedToRegister by viewModel.allowedToRegister.collectAsState()
    val registering by viewModel.registering.collectAsState()

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Let's create an account!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        FieldInstructionText(
            text = buildAnnotatedString {
                withStyle(
                    style =
                        if (emailError) SpanStyle(color = Color.Red)
                        else SpanStyle(color = Color.Black)
                ) {
                    append("Enter a valid email address.")
                }
            }
        )
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
        FieldInstructionText(
            text = buildAnnotatedString {
                append("Password must be at ")
                withStyle(
                    style =
                        if (passwordError[0]) SpanStyle(color = Color.Red)
                        else SpanStyle(color = Color.Black)
                ) {
                    append("least 8 characters long")
                }
                append(", has at ")
                withStyle(
                    style =
                        if (passwordError[1]) SpanStyle(color = Color.Red)
                        else SpanStyle(color = Color.Black)
                ) {
                    append("least one upper case letter")
                }
                append(", ")
                withStyle(
                    style =
                        if (passwordError[2]) SpanStyle(color = Color.Red)
                        else SpanStyle(color = Color.Black)
                ) {
                    append("one number")
                }
                append(", and ")
                withStyle(
                    style =
                        if (passwordError[3]) SpanStyle(color = Color.Red)
                        else SpanStyle(color = Color.Black)
                ) {
                    append("one symbol")
                }
                append(".")
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            label = {
                Text("Password")
            },
            shape = field_shape,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
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
        Spacer(modifier = Modifier.height(16.dp))
        FieldInstructionText(
            text = buildAnnotatedString {
                withStyle(
                    style =
                        if (confirmPasswordError) SpanStyle(color = Color.Red)
                        else SpanStyle(color = Color.Black)
                ) {
                    append("Please re-enter the password correctly.")
                }
            }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = confirmPassword,
            label = {
                Text("Re-enter Password")
            },
            shape = field_shape,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (allowedToRegister) {
                        viewModel.registerUserWithEmail(
                            context,
                            firebaseAuth,
                            mainNavController!!
                        )
                    }
                }
            ),
            visualTransformation =
                if (confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            onValueChange = {
                viewModel.updateConfirmPassword(it)
            },
            trailingIcon = {
                val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                val image =
                    if (confirmPasswordVisible) Icons.Outlined.Visibility
                    else Icons.Outlined.VisibilityOff
                IconButton(onClick = {
                    confirmPasswordVisible = !confirmPasswordVisible
                }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = button_shape,
            enabled = allowedToRegister,
            onClick = {
                viewModel.registerUserWithEmail(
                    context,
                    firebaseAuth,
                    mainNavController!!
                )
            }
        ) {
            Surface (color = Color.Transparent) {
                AnimatedVisibility(
                    visible = registering,
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
                    visible = !registering,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                ) {
                    Text("Register")
                }
            }
        }
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = button_shape,
            onClick = {
                welcomePageNavController!!.navigate(
                    route = WelcomeScreens.LoginContent.route
                )
            }
        ) {
            Text("Back")
        }
    }
}

@Preview
@Composable
fun RegistrationContentPreview() {
    RegistrationContent(null, null)
}