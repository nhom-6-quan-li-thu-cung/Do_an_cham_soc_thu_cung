package com.example.chamsocthucung2.view.login

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.MainActivity
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.viewmodel.login.LoginState
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MainScreen(navController: NavHostController, mAuth: FirebaseAuth, googleSignIn: () -> Unit,onSignSuccess:()-> Unit) {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var isLoggingIn by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var isUserLoggedIn by remember {  mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 🐶 Ảnh minh họa
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color(0xFFEDCD68), shape = RoundedCornerShape(100.dp))
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cat_dog),
                contentDescription = "Dog and Cat",
                modifier = Modifier.size(170.dp).align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Xin chào", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // 📧 Nhập email
        OutlinedTextField(
            value = emailValue,
            onValueChange = { emailValue = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔒 Nhập mật khẩu
        OutlinedTextField(
            value = passwordValue,
            onValueChange = { passwordValue = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🆕 Đăng ký & Quên mật khẩu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { navController.navigate("register_screen") }) {
                Text("Đăng ký tài khoản", fontWeight = FontWeight.Bold, color = Color.Black)
            }
            TextButton(onClick = { navController.navigate("forgot_password_screen") }) {
                Text("Quên mật khẩu?", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ⚡ Xử lý đăng nhập email & mật khẩu
        Button(
            onClick = {
                isLoggingIn = true
                mAuth.signInWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener { task ->
                        isLoggingIn = false
                        if (task.isSuccessful) {
                            navController.navigate("tao_ho_so") // ✅ Thành công → Chuyển trang
                        } else {
                            loginError = "Đăng nhập thất bại: ${task.exception?.message}"
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863)),
            enabled = !isLoggingIn
        ) {
            if (isLoggingIn) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Đăng nhập", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        // 🔴 Hiển thị lỗi nếu có
        loginError?.let {
            Text(it, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🟢 Nút Đăng nhập bằng Google
        GoogleSignInButton (onClick = {
            googleSignIn()
            onSignSuccess()
        }
        )
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Đăng nhập bằng Google", fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun RegisterScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Đăng ký tài khoản", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Họ và tên") },
            shape = RoundedCornerShape(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Số điện thoại") },
            shape = RoundedCornerShape(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            shape = RoundedCornerShape(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập lại mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(32.dp)
        )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                // Hình nền chỉ nằm ở nửa dưới màn hình
                Image(
                    painter = painterResource(id = R.drawable.image_nen_login), // Thay đổi tên hình nền tại đây
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(300.dp) // Thay đổi kích thước hình nền cho phù hợp
                )
            }
            Button(
                onClick = { navController.navigate(Routes.HOME) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863)),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(
                    "Hoàn thành",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }


@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    var emailPhoneValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp)
            .imePadding().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Quên mật khẩu",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = emailPhoneValue,
            onValueChange = { emailPhoneValue = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email hoặc số điện thoại") },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            // Hình nền chỉ nằm ở nửa dưới màn hình
            Image(
                painter = painterResource(id = R.drawable.image_nen_login), // Thay đổi tên hình nền tại đây
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(300.dp) // Thay đổi kích thước hình nền cho phù hợp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                // Điều hướng đến màn hình "ConfirmNewPasswordScreen"
                navController.navigate(Routes.CONFIRM_NEW_PASSWORD)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863))
        ) {
            Text(
                "Xác nhận",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ConfirmNewPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") } // State cho mật khẩu
    var confirmPassword by remember { mutableStateOf("") } // State cho xác nhận mật khẩu
    var isPasswordVisible by remember { mutableStateOf(false) } // State để kiểm tra mật khẩu có hiển thị hay không
    var isConfirmPasswordVisible by remember { mutableStateOf(false) } // State cho mật khẩu xác nhận có hiển thị hay không

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Thêm khả năng cuộn khi bàn phím xuất hiện
            .imePadding(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Xác nhận mật khẩu mới",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Nhập mật khẩu mới",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mật khẩu") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(32.dp),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Nhập lại mật khẩu",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập lại mật khẩu") },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(32.dp),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle Confirm Password Visibility"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            // Hình nền chỉ nằm ở nửa dưới màn hình
            Image(
                painter = painterResource(id = R.drawable.image_nen_login), // Thay đổi tên hình nền tại đây
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(300.dp) // Thay đổi kích thước hình nền cho phù hợp
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Spacer để đẩy nút "Hoàn tất" xuống dưới

        Button(
            onClick = {
                navController.navigate(Routes.VERIFY_PASSWORD) // ✅ Điều hướng đến màn hình xác thực OTP

                println("Mật khẩu mới: $password, Xác nhận mật khẩu: $confirmPassword")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863))
        ) {
            Text(
                "Hoàn tất",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyPasswordScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Xác thực mật khẩu",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Mã xác thực OTP",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            repeat(6) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gửi lại mã OTP",
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF0A109)
        )

        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            // Hình nền chỉ nằm ở nửa dưới màn hình
            Image(
                painter = painterResource(id = R.drawable.image_nen_login), // Thay đổi tên hình nền tại đây
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(300.dp) // Thay đổi kích thước hình nền cho phù hợp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navController = rememberNavController())
}
@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen(navController = rememberNavController())
}
@Preview(showBackground = true)
@Composable
fun PreviewConfirmNewPasswordScreen() {
    ConfirmNewPasswordScreen(navController= rememberNavController())
}
@Preview(showBackground = true)
@Composable
fun PreviewVerifyPasswordScreen() {
    VerifyPasswordScreen()
}
