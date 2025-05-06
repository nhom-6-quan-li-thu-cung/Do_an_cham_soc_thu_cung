package com.example.chamsocthucung2.viewmodel.login

import android.app.Application
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.local.firebase.AuthManager
import com.example.chamsocthucung2.data.repository.login.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuthUserCollisionException

// 📌 Trạng thái đăng nhập
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userEmail: String) : LoginState()
    data class Failure(val errorMessage: String) : LoginState()
    object PasswordResetEmailSent : LoginState()  // 📌 Đã gửi email đặt lại mật khẩu
    data class SuccessWithRole(val email: String, val role: String) : LoginState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)
    val authRepository = AuthRepository(application) // Nếu bạn có context
    private val auth = FirebaseAuth.getInstance()
    private val _loginStatus = MutableLiveData<LoginState>(LoginState.Idle)
    val loginStatus: LiveData<LoginState> = _loginStatus
    private val authmanger = AuthManager()

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.postValue(LoginState.Loading)
            val result = repository.signInWithEmail(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()?.user
                val uid = user?.uid

                if (uid != null) {
                    val role = authmanger.getUserRoleSuspend()
                    if (role != "unknown") {
                        _loginStatus.postValue(LoginState.SuccessWithRole(email, role))
                    } else {
                        _loginStatus.postValue(LoginState.Failure("Không xác định được vai trò người dùng"))
                    }
                } else {
                    _loginStatus.postValue(LoginState.Failure("Không xác định được UID người dùng"))
                }
            } else {
                _loginStatus.postValue(
                    LoginState.Failure("Đăng nhập thất bại: ${result.exceptionOrNull()?.message}")
                )
            }
        }
    }

    // ☁️ ĐĂNG NHẬP GOOGLE (Khởi tạo SignIn Intent)
    fun startGoogleSignIn(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            repository.beginGoogleSignIn(launcher)
        }
    }

    // ☁️ XỬ LÝ KẾT QUẢ GOOGLE SIGN-IN
    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            _loginStatus.postValue(LoginState.Loading)
            val result = repository.handleGoogleSignIn(data)
            if (result.isSuccess) {
                val user = result.getOrNull()
                _loginStatus.postValue(LoginState.Success(user?.email ?: "Unknown"))
            } else {
                _loginStatus.postValue(LoginState.Failure(result.exceptionOrNull()?.message ?: "Đăng nhập Google thất bại"))
            }
        }
    }

    // 📝 ĐĂNG KÝ + LƯU THÔNG TIN NGƯỜI DÙNG MỚI VÀO FIRESTORE
    // ───────────────────────────────────────────────
    fun registerUserWithInfo(
        email: String,
        password: String,
        fullName: String,
        phone: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val userInfo = mapOf(
                        "fullName" to fullName,
                        "phone" to phone,
                        "email" to email,
                        "role" to "user"
                    )

                    Firebase.firestore.collection("users").document(userId)
                        .set(userInfo)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onError("Lỗi khi lưu thông tin: ${e.message}")
                        }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        onError("Email đã được đăng ký.")
                    } else {
                        onError(exception?.message ?: "Đăng ký thất bại. Vui lòng thử lại.")
                    }
                }
            }
    }

    // Đăng xuất và xóa thông tin đăng nhập
    fun logout() {
        repository.clearUserSession()
        _loginStatus.value = LoginState.Idle
    }


    // ─────────────────────────────────────────────
    // 📧 Gửi link đặt lại mật khẩu qua Email
    // ─────────────────────────────────────────────
    fun forgotPassword(email: String) {
        _loginStatus.value = LoginState.Loading
        viewModelScope.launch {
            val result = authRepository.sendPasswordReset(email)
            _loginStatus.value = if (result.isSuccess) {
                LoginState.PasswordResetEmailSent
            } else {
                LoginState.Failure(result.exceptionOrNull()?.message ?: "Đã xảy ra lỗi khi gửi email")
            }
        }
    }

    fun resetLoginState() {
        _loginStatus.value = LoginState.Idle
    }

}
