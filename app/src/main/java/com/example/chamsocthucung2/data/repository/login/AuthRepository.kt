package com.example.chamsocthucung2.data.repository.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository(application: Application) {

    private val auth = FirebaseAuth.getInstance()
    private val oneTapClient: SignInClient = Identity.getSignInClient(application)
    private val context = application.applicationContext
    private var verificationId: String? = null

    // ───────────────────────────────────────────────
    // 🔐 EMAIL/PASSWORD
    // ───────────────────────────────────────────────

    suspend fun signInWithEmail(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result)
        } catch (e: Exception) {
            Log.e("AuthRepository", "❌ Email sign-in failed", e)
            Result.failure(e)
        }
    }

    // ───────────────────────────────────────────────
    // 🌐 GOOGLE SIGN-IN (One Tap API)
    // ───────────────────────────────────────────────

    suspend fun beginGoogleSignIn(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        try {
            val signInRequest = BeginSignInRequest.Builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("176126295749-1d83t9jbloe9m5lj7d4jfg8d7u3rnjsq.apps.googleusercontent.com")
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(true)
                .build()

            val result = oneTapClient.beginSignIn(signInRequest).await()
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
            launcher.launch(intentSenderRequest)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "❌ Begin SignIn failed", e)
        }
    }

    suspend fun handleGoogleSignIn(data: Intent?): Result<FirebaseUser> {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken == null) return Result.failure(Exception("ID Token null"))

            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(firebaseCredential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "❌ Handle SignIn failed", e)
            Result.failure(e)
        }
    }
    private val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
//
//    // Lưu UID và email vào SharedPreferences
//    fun saveUserSession(uid: String, email: String) {
//        val editor = sharedPreferences.edit()
//        editor.putString("user_uid", uid)
//        editor.putString("user_email", email)
//        editor.apply()
//    }
//
//    // Lấy UID và email từ SharedPreferences
//    fun getUserSession(): Pair<String?, String?> {
//        val uid = sharedPreferences.getString("user_uid", null)
//        val email = sharedPreferences.getString("user_email", null)
//        return Pair(uid, email)
//    }

    // Xóa thông tin đăng nhập khi đăng xuất
    fun clearUserSession() {
        // Xóa SharedPreferences
        sharedPreferences.edit().clear().apply()
        // Xóa dữ liệu Firebase Auth (nếu dùng)
        Firebase.auth.signOut()
        // Xóa các biến tạm khác (nếu cần)
    }
    // ───────────────────────────────────────────────
    // 🔐 QUÊN MẬT KHẨU (EMAIL & SỐ ĐIỆN THOẠI)
    // ───────────────────────────────────────────────

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "❌ Gửi email đặt lại mật khẩu thất bại", e)
            Result.failure(e)
        }
    }

    fun getStoredVerificationId(): String? = verificationId
}