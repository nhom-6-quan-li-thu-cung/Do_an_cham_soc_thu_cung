package com.example.chamsocthucung2.view.doctor

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import androidx.navigation.NavHostController
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.viewmodel.doctor.logindoctor.DoctorViewModel
import com.example.chamsocthucung2.viewmodel.doctor.logindoctor.LoginState

@SuppressLint("RestrictedApi")
@Composable
fun DoctorLoginScreen(
    navController: NavHostController,
    viewModel: DoctorViewModel,
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    var emailValue by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var passwordValue by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val emailPattern = Patterns.EMAIL_ADDRESS

    val loginState by viewModel.loginStatus.observeAsState(LoginState.Idle)

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.DOCTOR_INFO) {
                    popUpTo(Routes.DOCTOR_LOGIN) { inclusive = true }
                }
            }
            is LoginState.Failure -> {
                Toast.makeText(context, (loginState as LoginState.Failure).errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color(0xFFEDCD68), shape = RoundedCornerShape(100.dp))
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cat_dog),
                contentDescription = null,
                modifier = Modifier
                    .size(170.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Đăng nhập với tư cách bác sĩ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emailValue,
            onValueChange = {
                emailValue = it
                isEmailValid = emailPattern.matcher(it).matches()
            },
            label = { Text("Email") },
            isError = !isEmailValid,
            supportingText = {
                if (!isEmailValid) Text("Email không hợp lệ", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordValue,
            onValueChange = { passwordValue = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (emailValue.isBlank() || passwordValue.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                } else if (!isEmailValid) {
                    Toast.makeText(context, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.signInWithEmail(emailValue.trim(), passwordValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863))
        ) {
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Đăng nhập", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        if (loginState is LoginState.Failure) {
            Text(
                text = (loginState as LoginState.Failure).errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
