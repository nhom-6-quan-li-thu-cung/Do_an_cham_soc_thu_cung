package com.example.chamsocthucung2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.data.local.firebase.AuthManager
import com.example.chamsocthucung2.navigation.NavGraph
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.ui.theme.ChamSocThuCung2Theme
import com.example.chamsocthucung2.view.user.Profile.PetLottieAnimation
import com.example.chamsocthucung2.viewmodel.AppViewModel
import com.example.chamsocthucung2.viewmodel.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val appViewModel: AppViewModel by viewModels()
    private lateinit var googleSignInLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    loginViewModel.handleGoogleSignInResult(result.data)
                } else {
                    Log.e("GoogleSignIn", "Đăng nhập Google bị hủy hoặc thất bại")
                    Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
                }
            }

        setContent {
            val navController = rememberNavController()
            var currentThemeColor by remember { mutableStateOf(Color(0xFFF8E7C0)) }
            var startDestination by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                val authManager = AuthManager()
                if (authManager.isLogged()) {
                    val role = authManager.getUserRoleSuspend()
                    startDestination = when (role) {
                        "doctor" -> Routes.HOMEDOCTOR
                        "user" -> Routes.HOME
                        else -> Routes.MAIN
                    }
                } else {
                    startDestination = Routes.MAIN
                }
            }

            ChamSocThuCung2Theme {
                if (startDestination != null) {
                    NavGraph(
                        navController = navController,
                        appViewModel = appViewModel,
                        mAuth = FirebaseAuth.getInstance(),
                        loginViewModel = loginViewModel,
                        googleSignIn = {
                            loginViewModel.startGoogleSignIn(googleSignInLauncher)
                        },
                        onSignSuccess = {
                            // Sau đăng nhập, ViewModel có thể điều hướng tiếp
                        },
                        signOut = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(Routes.MAIN) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
                        },
                        changeTheme = { color ->
                            currentThemeColor = color
                        },
                        startDestination = startDestination!!
                    )
                } else {
                    // Hiển thị màn hình loading trong lúc đợi xác định phân luồng người dùng
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PetLottieAnimation()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Đang tải...", fontSize = 16.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
