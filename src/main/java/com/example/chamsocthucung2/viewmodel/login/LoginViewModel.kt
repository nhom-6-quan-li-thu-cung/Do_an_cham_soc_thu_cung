package com.example.chamsocthucung2.viewmodel.login

import android.app.Application
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userEmail: String) : LoginState()
    data class Failure(val errorMessage: String) : LoginState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val _loginStatus = MutableLiveData<LoginState>(LoginState.Idle) // ✅
    val loginStatus: LiveData<LoginState> = _loginStatus // ✅ LiveData để quan sát

    init {
        // Cấu hình Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(application, gso)
    }

    // Hàm đăng nhập Google
    fun signIn(activity: ComponentActivity) {
        signOut {
            val signInIntent = googleSignInClient.signInIntent
            activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    // Hàm đăng xuất Google + Firebase
    fun signOut(onComplete: () -> Unit) {
        mAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                _loginStatus.postValue(LoginState.Idle) // ✅ Trả trạng thái về Idle sau khi đăng xuất
                onComplete()
            }
        }
    }

    // Hàm xử lý đăng nhập với Firebase sau khi Google Sign-In thành công
    fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginStatus.postValue(LoginState.Loading) // ✅

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userEmail = task.result?.user?.email ?: "Unknown"
                        _loginStatus.postValue(LoginState.Success(userEmail)) // ✅
                    } else {
                        _loginStatus.postValue(LoginState.Failure("Đăng nhập thất bại")) // ✅
                    }
                }
        }
    }

    // Hàm xử lý kết quả từ ActivityResult
    fun handleSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                _loginStatus.postValue(LoginState.Failure("Google Sign-In Failed: ${e.message}")) // ✅
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 100
    }
}
