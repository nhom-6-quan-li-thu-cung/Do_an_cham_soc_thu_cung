package com.example.chamsocthucung2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.navigation.NavGraph
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.ui.theme.Chamsocthucung2Theme
import com.example.chamsocthucung2.view.login.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                authViewModel.handleSignInResult(result.data, navController) // Sử dụng navController
            } else {
                Log.e("GoogleSignIn", "Đăng nhập Google bị hủy hoặc thất bại")
                Toast.makeText(this, "Đăng nhập Google bị hủy hoặc thất bại", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            navController = rememberNavController()
            var currentThemeColor by remember { mutableStateOf(Color(0xFFF8E7C0)) }

            Chamsocthucung2Theme {
                NavGraph(
                    navController = navController,
                    mAuth = FirebaseAuth.getInstance(), // Vẫn có thể truyền để dùng cho đăng nhập email/pass
                    googleSignIn = { authViewModel.signInWithGoogle(googleSignInLauncher) },
                    onSignSuccess = { /* Không cần callback trực tiếp ở đây nữa */ },
                    signOut = {
                        FirebaseAuth.getInstance().signOut()
                        // Cần thêm logic đăng xuất Google nếu cần
                        navController.navigate(Routes.MAIN) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
                    },
                    changeTheme = { color ->
                        currentThemeColor = color
                    }
                )

            }
        }
    }

    private lateinit var navController: androidx.navigation.NavHostController
}