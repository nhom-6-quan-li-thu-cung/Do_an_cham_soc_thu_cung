package com.example.chamsocthucung2.view.user.Profile

import com.example.chamsocthucung2.data.repository.user.UserProfile

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val pet: UserProfile?) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}