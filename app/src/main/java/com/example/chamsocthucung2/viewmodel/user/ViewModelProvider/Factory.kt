package com.example.chamsocthucung2.viewmodel.user.ViewModelProvider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chamsocthucung2.data.repository.user.ProfileRoom
import com.example.chamsocthucung2.viewmodel.user.ProfileRoomViewModel

class ProfileRoomViewModelFactory(
    private val repository: ProfileRoom
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileRoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileRoomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
