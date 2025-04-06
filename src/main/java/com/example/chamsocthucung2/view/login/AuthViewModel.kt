package com.example.chamsocthucung2.view.login

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val signInClient: SignInClient = Identity.getSignInClient(application)

    fun signInWithGoogle(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            try {
                // 🔥 Đăng xuất tài khoản hiện tại nếu có để chọn tài khoản khác
                signInClient.signOut().await()
                firebaseAuth.signOut()

                val signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId("176126295749-1d83t9jbloe9m5lj7d4jfg8d7u3rnjsq.apps.googleusercontent.com")
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    .build()

                val signInResult = signInClient.beginSignIn(signInRequest).await()
                val intentSenderRequest = IntentSenderRequest.Builder(signInResult.pendingIntent).build()
                launcher.launch(intentSenderRequest)

            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Lỗi khi đăng nhập Google", e)
            }
        }
    }

    fun handleSignInResult(data: Intent?, navController: NavController) {
        val credential = signInClient.getSignInCredentialFromIntent(data)
        val idToken = credential.googleIdToken

        if (idToken != null) {
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Log.d("GoogleSignIn", "Đăng nhập thành công")

                        if (user != null) {
                            val hasProfile = checkUserProfile(user.uid)
                            if (hasProfile) {
                                navController.navigate("home_screen") // ✅ Đã có hồ sơ → Home
                            } else {
                                navController.navigate("tao_ho_so") // ✅ Chưa có hồ sơ → Tạo hồ sơ
                            }
                        }
                    } else {
                        Log.e("GoogleSignIn", "Đăng nhập thất bại", task.exception)
                    }
                }
        }
    }

    // 🔍 Hàm kiểm tra hồ sơ người dùng (Cần thay thế bằng Firebase Firestore)
    private fun checkUserProfile(userId: String): Boolean {
        return false // 🔥 Thay bằng truy vấn Firestore thực tế
    }
}
