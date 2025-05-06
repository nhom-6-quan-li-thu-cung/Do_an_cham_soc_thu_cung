package com.example.chamsocthucung2.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chamsocthucung2.data.repository.user.ProfileRepositoryImpl

class ProfileViewModelFactory(
    private val profileRepository: ProfileRepositoryImpl // Giữ nguyên nếu bạn muốn tạo Impl bên trong
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val profileRepositoryImpl = ProfileRepositoryImpl()
            return ProfileViewModel(profileRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}