package com.samedtemiz.fitlife.ui.screens.auth

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.samedtemiz.fitlife.R
import com.samedtemiz.fitlife.ui.app.AppSettings
import com.samedtemiz.fitlife.components.ButtonComponent
import com.samedtemiz.fitlife.components.ClickableTextComponent
import com.samedtemiz.fitlife.components.NormalTextBoxComponent
import com.samedtemiz.fitlife.components.PasswordTextBoxComponent
import com.samedtemiz.fitlife.data.auth.login.LoginViewModel
import com.samedtemiz.fitlife.data.auth.login.LoginFormEvent
import com.samedtemiz.fitlife.data.auth.login.LoginUIState
import com.samedtemiz.fitlife.navigation.AppRouter
import com.samedtemiz.fitlife.navigation.Screen

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {

    val state = loginViewModel.state
    val isDarkMode = AppSettings.isDarkMode(LocalContext.current)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(
                    id = if (isDarkMode) R.drawable.food_bg_dark else R.drawable.food_bg_light
                ),
                contentDescription = "Login",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(6.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp)
                    .alpha(0.6f)
                    .clip(
                        CutCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.background)
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                LoginHeader()

                LoginFields(loginViewModel, state)

                LoginFooter(
                    onLoginClick = {
                        loginViewModel.onEvent(LoginFormEvent.Submit)
                    },
                    onRegisterClick = {
                        AppRouter.navigateTo(Screen.RegisterScreen)
                    },
                    enabledStatus = true
                )

            }


        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Welcome Back",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(
                Font(R.font.esprit_bold)
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Sign in to continue",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(
                Font(R.font.esprit_bold)
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LoginFields(loginViewModel: LoginViewModel, state: LoginUIState) {
    Column {
        if (state.loginError != null) {
            Text(text = state.loginError, color = Color.Red)
        }
        //EMAIL
        NormalTextBoxComponent(
            label = "Email",
            placeholder = "Enter your email address",
            supportingText = state.emailError ?: "unknown error",
            onTextSelected = {
                loginViewModel.onEvent(LoginFormEvent.EmailChanged(it))
            },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            errorStatus = state.emailError != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        val showPassword = remember { mutableStateOf(false) }
        //PASSWORD
        PasswordTextBoxComponent(
            label = "Password",
            placeholder = "Enter your password",
            supportingText = state.passwordError ?: "unknown error",
            onTextSelected = {
                loginViewModel.onEvent(LoginFormEvent.PasswordChanged(it))
            },
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val (icon, iconColor) = if (showPassword.value) {
                    Pair(
                        Icons.Filled.Visibility,
                        Color(0xFF40484d)
                    )
                } else {
                    Pair(Icons.Filled.VisibilityOff, Color(0xFF40484d))
                }

                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        icon,
                        contentDescription = "Visibility",
                        tint = iconColor
                    )
                }
            },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            errorStatus = state.passwordError != null
        )

        TextButton(
            onClick = {}, modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(
                text = "Forgot your password?", fontFamily = FontFamily(
                    Font(R.font.esprit_bold)
                )
            )
        }
    }
}

@Composable
fun LoginFooter(
    onLoginClick: () -> Unit,
    onRegisterClick: (String) -> Unit,
    enabledStatus: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ButtonComponent(
            value = "Login",
            onButtonClicked = onLoginClick,
            isEnabled = enabledStatus
        )

        Spacer(modifier = Modifier.height(5.dp))

        ClickableTextComponent(
            tryingToLogin = true,
            onTextSelected = onRegisterClick
        )
    }
}