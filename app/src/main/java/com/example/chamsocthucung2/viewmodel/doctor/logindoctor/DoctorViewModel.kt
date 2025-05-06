package com.example.chamsocthucung2.viewmodel.doctor.logindoctor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.repository.doctor.login.DoctorRepository
import com.example.chamsocthucung2.data.repository.login.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

// Trạng thái đăng nhập
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userEmail: String) : LoginState()
    data class Failure(val errorMessage: String) : LoginState()
}

class DoctorViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()
    private val doctorRepository = DoctorRepository(firestore, application)
    private val authRepository = AuthRepository(application)  // Sử dụng AuthRepository

    private val _loginStatus = MutableLiveData<LoginState>()
    val loginStatus: LiveData<LoginState> = _loginStatus

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.postValue(LoginState.Loading)

            val result = authRepository.signInWithEmail(email, password)

            if (result.isSuccess) {
                val user = result.getOrNull()?.user
                if (user != null) {
                    // Di chuyển dữ liệu từ users sang doctors
                    val migrateResult = doctorRepository.migrateUserToDoctor(user.uid)
                    if (migrateResult.isSuccess) {
                        _loginStatus.postValue(LoginState.Success(user.email ?: ""))
                    } else {
                        _loginStatus.postValue(LoginState.Failure("❌ Không thể chuyển dữ liệu sang bác sĩ: ${migrateResult.exceptionOrNull()?.message}"))
                    }
                } else {
                    _loginStatus.postValue(LoginState.Failure("❌ Không tìm thấy người dùng sau khi đăng nhập"))
                }
            } else {
                _loginStatus.postValue(LoginState.Failure("❌ Đăng nhập thất bại: ${result.exceptionOrNull()?.message}"))
            }
        }
    }
}
