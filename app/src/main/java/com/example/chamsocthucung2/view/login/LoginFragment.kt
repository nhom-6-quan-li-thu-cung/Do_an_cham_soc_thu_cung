package com.example.chamsocthucung2.view.login

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.viewmodel.login.LoginState
import com.example.chamsocthucung2.viewmodel.login.LoginViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    // Launcher cho Identity API
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            viewModel.handleGoogleSignInResult(data)
        }
    }

    // State cho email/password
    var emailValue by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var passwordValue by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val emailPattern = Patterns.EMAIL_ADDRESS

    // Quan sát trạng thái đăng nhập
    val loginState by viewModel.loginStatus.observeAsState(LoginState.Idle)

    // Điều hướng sau khi đăng nhập thành công
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.SuccessWithRole -> {
                val role = (loginState as LoginState.SuccessWithRole).role
                if (role == "doctor") {
                    navController.navigate(Routes.HOMEDOCTOR) {
                        popUpTo("main_screen") { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.HOME) {
                        popUpTo("main_screen") { inclusive = true }
                    }
                }
            }
            is LoginState.Failure -> {
                Toast.makeText(context, (loginState as LoginState.Failure).errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 🐶 Ảnh minh họa
        Box(
            modifier = Modifier
                .fillMaxWidth() // Để box chiếm toàn bộ chiều ngang
                .height(220.dp) // Điều chỉnh chiều cao cho phù hợp với ảnh
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 90.dp, // Bo tròn góc dưới bên trái
                        bottomEnd = 90.dp // Bo tròn góc dưới bên phải
                    )
                )
                .background(Color(0xFFFFE0B2)), // Màu nền box vàng nhạt
            contentAlignment = Alignment.TopCenter // Căn chỉnh nội dung (ảnh) lên trên
        ) {
            Image(
                painter = painterResource(id = R.drawable.cat_dog),
                contentDescription = "Dog and Cat",
                modifier = Modifier
                    .size(180.dp) // Tăng kích thước ảnh cho phù hợp với box
                    .padding(top = 10.dp) // Thêm padding phía trên để ảnh không bị sát mép
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Xin chào",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "“Yêu thương không giới hạn, chăm sóc không ngừng và tận tình chu đáo.”",
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        // 📧 Nhập email
        OutlinedTextField(
            value = emailValue,
            onValueChange = {
                emailValue = it
                isEmailValid = emailPattern.matcher(it).matches()
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email", color = Color.Black) },
            isError = !isEmailValid,
            supportingText = {
                if (!isEmailValid) {
                    Text("Email không hợp lệ", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordValue,
            onValueChange = { passwordValue = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mật khẩu", color = Color.Black) },
            shape = RoundedCornerShape(24.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Default.Visibility
                else Icons.Default.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu", tint = Color.Black)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

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

        // 🟡 Nút Đăng nhập
        Button(
            onClick = {
                if (isEmailValid) {
                    viewModel.signInWithEmail(emailValue.trim(), passwordValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863)),
            enabled = loginState !is LoginState.Loading,
            shape = RoundedCornerShape(24.dp)
        ) {
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Đăng nhập",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
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
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "OR", color = Color.Black, fontSize=14.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔵 Đăng nhập với Google
        GoogleSignInButton(
            onClick = { viewModel.startGoogleSignIn(launcher) }
        )
        LaunchedEffect(loginState) {
            if (loginState is LoginState.Success) {
                // Kiểm tra xem đây có phải là lần đăng nhập đầu tiên không
                val user = FirebaseAuth.getInstance().currentUser
                val userEmail = user?.email

                if (user != null) {
                    // Kiểm tra xem user đã tạo mật khẩu hay chưa
                    val isPasswordSet = user.providerData.any { it.providerId == "password" }

                    if (isPasswordSet) {
                        // Nếu người dùng đã tạo mật khẩu, chuyển đến màn hình Home
                        navController.navigate(Routes.HOME) {
                            popUpTo("main_screen") { inclusive = true }
                        }
                    } else {
                        // Nếu người dùng chưa tạo mật khẩu, chuyển đến màn hình tạo mật khẩu
                        navController.navigate(Routes.CONFIRM_NEW_PASSWORD) {
                            popUpTo("main_screen") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo), // Đảm bảo bạn có drawable này
                contentDescription = "Đăng nhập với Google",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Để hiển thị màu gốc của logo
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Đăng nhập với Google",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}


//@Composable
//fun GoogleSignInButton(onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.White,
//            contentColor = Color.Black
//        ),
//        border = BorderStroke(1.dp, Color.Gray)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_google_logo),
//                contentDescription = "Google Logo",
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Text("Đăng nhập bằng Google", fontWeight = FontWeight.Bold)
//        }
//    }
//}


@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
    onRegistrationSuccess: (Map<String, String>) -> Unit // Thêm callback này
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val loginState by viewModel.loginStatus.observeAsState(LoginState.Idle)
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            // Gọi callback khi đăng ký thành công và truyền dữ liệu
            onRegistrationSuccess(mapOf("hoTen" to fullName, "email" to email, "sdt" to phone))
            // Không tự điều hướng ở đây nữa, việc điều hướng sẽ được xử lý trong NavGraph
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Cho phép cuộn
            .imePadding(),                          // Đẩy UI lên khi bàn phím xuất hiện
        verticalArrangement = Arrangement.Top
    )
    {
        // Header với nút quay lại và tiêu đề căn giữa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại"
                )
            }
            Text(
                text = "Đăng ký tài khoản",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val nameRegex = Regex("^[\\p{L} ]+\$")
        OutlinedTextField(
            value = fullName,
            onValueChange = {
                if (it.isEmpty() || it.matches(nameRegex)) {
                    fullName = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Họ và tên") },
            shape = RoundedCornerShape(32.dp),
            isError = fullName.isNotEmpty() && !fullName.matches(nameRegex)
        )
        if (fullName.isNotEmpty() && !fullName.matches(nameRegex)) {
            Text(
                text = "Tên chỉ được chứa chữ cái",
                color = Color.Red,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    phone = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Số điện thoại") },
            shape = RoundedCornerShape(32.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            shape = RoundedCornerShape(32.dp),
            isError = email.isNotEmpty() && !email.matches(emailRegex),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        if (email.isNotEmpty() && !email.matches(emailRegex)) {
            Text(
                text = "Email không hợp lệ",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }
        val passwordsMatch = password == confirmPassword || confirmPassword.isEmpty()
// Điều kiện kiểm tra mật khẩu không trùng
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mật khẩu") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Default.Visibility
                else Icons.Default.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            shape = RoundedCornerShape(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập lại mật khẩu") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible)
                    Icons.Default.Visibility
                else Icons.Default.VisibilityOff

                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            isError = !passwordsMatch,
            shape = RoundedCornerShape(32.dp)
        )
// Thông báo lỗi nếu mật khẩu không khớp
        if (!passwordsMatch) {
            Text(
                text = "Mật khẩu không khớp",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

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
        var showError by remember { mutableStateOf(false) }
        val isLoading = loginState is LoginState.Loading

        val isFormValid = fullName.matches(Regex("^[\\p{L} ]+\$"))
                && phone.length >= 9 && phone.all { it.isDigit() }
                && email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"))
                && password.length >= 6
                && password == confirmPassword

        Button(
            onClick = {
                if (isFormValid) {
                    showError = false
                    viewModel.registerUserWithInfo(
                        email = email,
                        password = password,
                        fullName = fullName,
                        phone = phone,
                        role = "users",
                        onSuccess = {
                            // Gọi callback onRegistrationSuccess ở đây
                            onRegistrationSuccess(mapOf("hoTen" to fullName, "email" to email, "sdt" to phone, "role" to "users"))
                            // Không điều hướng trực tiếp nữa
                        },
                        onError = { error ->
                            showError = true
                            errorMessage = error
                        }
                    )
                } else {
                    showError = true
                    errorMessage = "Vui lòng kiểm tra lại thông tin đăng ký!"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863)),
            shape = RoundedCornerShape(32.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Hoàn thành",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
        if (showError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

    }
}


@SuppressLint("RestrictedApi")
@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    var emailPhoneValue by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    val isValidEmail = emailPhoneValue.matches(emailRegex)
    val isValidPhone = emailPhoneValue.all { it.isDigit() } && emailPhoneValue.length >= 9

    val isInputValid = isValidEmail || isValidPhone
    val viewModel: LoginViewModel = viewModel()
    val loginStatus by viewModel.loginStatus.observeAsState()
    LaunchedEffect(loginStatus) {
        if (loginStatus is LoginState.PasswordResetEmailSent) {
            navController.navigate(Routes.VERIFY_PASSWORD)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Quên mật khẩu",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.weight(1f))

            // Để giữ cho title ở giữa hoàn hảo, ta thêm một box kích thước bằng IconButton
            Box(modifier = Modifier.size(48.dp)) {}
        }
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = emailPhoneValue,
            onValueChange = {
                emailPhoneValue = it
                showError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            isError = showError && !isInputValid,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        if (showError && !isInputValid) {
            Text(
                text = "Email!",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image_nen_login),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(300.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Nút Xác nhận
        Button(
            onClick = {
                when {
                    isValidEmail -> {
                        viewModel.forgotPassword(emailPhoneValue) // gửi link reset
                    }
                    else -> {
                        showError = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863))
        ) {
            Text("Xác nhận", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }


// Lắng nghe trạng thái gửi email
        LaunchedEffect(loginStatus) {
            when (loginStatus) {
                is LoginState.PasswordResetEmailSent -> {
                    Toast.makeText(context, "📩 Đã gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Về màn đăng nhập
                }

                is LoginState.Failure -> {
                    Toast.makeText(context, (loginStatus as LoginState.Failure).errorMessage, Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }
    }
}


@Composable
fun ConfirmNewPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Nếu chưa đăng nhập qua Google hoặc chưa có user
    if (user == null) {
        navController.popBackStack() // Quay lại màn hình đăng nhập nếu chưa có user
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7C0))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        // Nút quay lại + tiêu đề căn giữa
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Tạo mật khẩu mới",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Tạo mật khẩu ",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mật khẩu") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Ẩn/Hiện mật khẩu"
                    )
                }
            },
            shape = RoundedCornerShape(32.dp)
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
            onValueChange = {
                confirmPassword = it
                isError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập lại mật khẩu") },
            isError = isError,
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Ẩn/Hiện xác nhận mật khẩu"
                    )
                }
            },
            shape = RoundedCornerShape(32.dp)
        )

        // Thông báo lỗi nếu mật khẩu không khớp
        if (isError) {
            Text(
                text = "Mật khẩu không khớp!",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image_nen_login),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (password == confirmPassword && password.isNotBlank()) {
                    // Liên kết tài khoản Google với mật khẩu
                    val email = user?.email
                    val credential = EmailAuthProvider.getCredential(email!!, password)

                    user?.linkWithCredential(credential)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Chuyển tới màn hình xác thực mật khẩu
                                navController.navigate(Routes.TAO_HO_SO)
                            } else {
                                // Xử lý lỗi nếu không liên kết được
                                Toast.makeText(context, "Không thể liên kết tài khoản!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    isError = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B863)),
            shape = RoundedCornerShape(32.dp)
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


//@SuppressLint("RestrictedApi")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VerifyPasswordScreen( phoneNumber: String, navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
//    val context = LocalContext.current
//    val focusRequesters = List(6) { FocusRequester() }
//    val otpValues = remember { List(6) { mutableStateOf("") } }
//    var isLoading by remember { mutableStateOf(false) }
//    val loginStatus by viewModel.loginStatus.observeAsState(LoginState.Idle)
//    val otpCode = otpValues.joinToString("") { it.value }
//    var isVerifying by remember { mutableStateOf(false) }
//
//
//    // Gửi mã xác thực khi nhập đủ 6 số
//    LaunchedEffect(otpCode) {
//        if (otpCode.length == 6 && !isVerifying) {
//            isVerifying = true
//            viewModel.verifyOtpCode(otpCode) // ✅ Gọi ViewModel xử lý OTP
//        }
//    }
//    // Theo dõi trạng thái đăng nhập
//    LaunchedEffect(loginStatus) {
//        when (loginStatus) {
//            is LoginState.Success -> {
//                Toast.makeText(context, "✅ Xác thực thành công", Toast.LENGTH_SHORT).show()
//                navController.navigate(Routes.HOME) {
//                    popUpTo(0) { inclusive = true } // clear back stack
//                }
//            }
//
//            is LoginState.Failure -> {
//                Toast.makeText(context, (loginStatus as LoginState.Failure).errorMessage, Toast.LENGTH_SHORT).show()
//                otpValues.forEach { it.value = "" }
//                focusRequesters[0].requestFocus()
//                isVerifying = false
//            }
//
//            else -> {}
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // UI chính
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFF8E7C0))
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//                .imePadding()
//        ) {
//            // Header
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
//                }
//                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = "Xác thực mật khẩu",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                Box(modifier = Modifier.size(48.dp)) {}
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text(
//                text = "Mã xác thực OTP",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                otpValues.forEachIndexed { index, value ->
//                    OutlinedTextField(
//                        value = value.value,
//                        onValueChange = {
//                            if (it.length <= 1 && it.all { char -> char.isDigit() }) {
//                                value.value = it
//                                if (it.isNotEmpty() && index < 5) {
//                                    focusRequesters[index + 1].requestFocus()
//                                }
//                            }
//                        },
//                        modifier = Modifier
//                            .size(48.dp)
//                            .padding(horizontal = 4.dp)
//                            .focusRequester(focusRequesters[index]),
//                        singleLine = true,
//                        textStyle = LocalTextStyle.current.copy(
//                            textAlign = TextAlign.Center,
//                            fontSize = 18.sp
//                        ),
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedTextColor = Color.Black
//                        ),
//                        enabled = !isLoading // ⛔ Không cho nhập khi đang loading
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Gửi lại mã OTP",
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFFF0A109),
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .clickable(enabled = !isLoading) {
//                        viewModel.resendOtp(
//                            phoneNumber = phoneNumber,
//                            activity = context as ComponentActivity,
//                            onSuccess = {
//                                Toast.makeText(context, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show()
//                            },
//                            onError = { error ->
//                                Toast.makeText(context, "Không thể gửi lại OTP: $error", Toast.LENGTH_LONG).show()
//                            }
//                        )
//                    }
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.CenterHorizontally)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.image_nen_login),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .size(300.dp)
//                )
//            }
//        }
//
//        // 🔄 Loading overlay
//        if (isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.3f)),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(color = Color.White)
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen(navController = rememberNavController())
}