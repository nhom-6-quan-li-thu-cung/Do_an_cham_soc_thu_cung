package com.example.chamsocthucung2.viewmodel.user

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.repository.user.ProfileRepositoryImpl
import com.example.chamsocthucung2.data.repository.user.UserProfile
import com.example.chamsocthucung2.view.user.Profile.ProfileUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val repository: ProfileRepositoryImpl
) : ViewModel() {

    private val _uiState = mutableStateOf<ProfileUiState>(ProfileUiState.Loading)
    private val _userProfileState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val userProfileState = _userProfileState.asStateFlow()
    val uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val isDarkMode = MutableStateFlow(false)
    private val _isDarkMode = mutableStateOf(false)

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile


    private val _userfullName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userfullName
    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail
    private val _avatarUrl = MutableLiveData<String?>()
    val avatarUrl: LiveData<String?> = _avatarUrl

    // Dùng để báo trạng thái đổi mật khẩu
    private val _passwordChangeState = MutableLiveData<Result<Unit>?>()
    val passwordChangeState: LiveData<Result<Unit>?> get() = _passwordChangeState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Load user info từ FirebaseAuth
    fun loadUserInfo() {
        val user = repository.getCurrentUserInfo()
        user?.let {
            _userfullName.value = it.displayName ?: "Không có tên"
            _userEmail.value = it.email ?: "Không có email"
            _avatarUrl.value = it.photoUrl?.toString()
        }
    }

    // ⚡ Đổi mật khẩu mới
    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val reauthSuccess = repository.reauthenticateUser(currentPassword)
            if (reauthSuccess) {
                val updateSuccess = repository.updatePassword(newPassword)
                if (updateSuccess) {
                    _passwordChangeState.value = Result.success(Unit)
                } else {
                    _passwordChangeState.value = Result.failure(Exception("Đổi mật khẩu thất bại"))
                }
            } else {
                _passwordChangeState.value = Result.failure(Exception("Mật khẩu hiện tại không đúng"))
            }
            _isLoading.value = false
        }
    }

    fun clearPasswordChangeState() {
        _passwordChangeState.value = null
    }

    fun loadGoogleUser() {
        viewModelScope.launch {
            try {
                val user = repository.getUserFromGoogle()
                if (user != null) {
                    _userProfile.value = user
                    _userProfileState.value = ProfileUiState.Success(pet = user)
                } else {
                    _userProfileState.value = ProfileUiState.Error("Không tìm thấy thông tin người dùng Google")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading Google user", e)
                _userProfileState.value = ProfileUiState.Error(e.message ?: "Đã xảy ra lỗi")
            }
        }
    }
    class UserViewModel(private val userRepository: ProfileRepositoryImpl.UserRepository) : ViewModel() {

        private val _avatarUrl = MutableStateFlow<String?>(null)
        val avatarUrl: StateFlow<String?> = _avatarUrl.asStateFlow()

        fun setAvatarUrl(url: String?) {
            _avatarUrl.value = url
        }

        fun uploadAndSaveAvatar(imageUri: Uri) {
            viewModelScope.launch {
                try {
                    val url = userRepository.uploadAvatar(imageUri)
                    userRepository.updateAvatarUrl(url)
                    _avatarUrl.value = url // cập nhật avatar mới lên UI
                } catch (e: Exception) {
                    // Xử lý lỗi nếu cần
                }
            }
        }
    }


    // Load profile từ Firestore
    fun loadFirestoreUser() {
        viewModelScope.launch {
            _userProfile.value = repository.getUserFromFirestore()
        }
    }

    //update tên
    fun updateUserfullName(newName: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseFirestore.getInstance().collection("users").document(uid)

        viewModelScope.launch {
            try {
                userRef.update("fullName", newName).await()
                _userfullName.postValue(newName) // Update luôn LiveData để UI tự đổi
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Update tên thất bại", e)
            }
        }
    }


    // Load user profile từ petRepository
    fun getUserProfile() {
        viewModelScope.launch {
            _userProfileState.value = ProfileUiState.Loading
            try {
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading user profile", e) // <-- thêm dòng này
                _userProfileState.value = ProfileUiState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    // Load pet profile
    fun loadPetProfile() {
        viewModelScope.launch {
            _uiState.value
                try {
            } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error loading user profile", e) // <-- thêm dòng này
                    ProfileUiState.Error(e.message ?: "Error loading profile")
            }
        }
    }

    // Toggle dark mode
    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }
}
