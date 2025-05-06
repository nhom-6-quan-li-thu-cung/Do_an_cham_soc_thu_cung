package com.example.chamsocthucung2.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.repository.user.ProfileRoom
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileRoomViewModel(
    private val repository: ProfileRoom
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _themeColor = MutableStateFlow("Cam")
    val themeColor: StateFlow<String> = _themeColor

    private val _fontSize = MutableStateFlow("Trung bình")
    val fontSize: StateFlow<String> = _fontSize

    private val currentUid: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    init {
        loadUserCustomization()
    }

    private fun loadUserCustomization() {
        viewModelScope.launch {
            try {
                val uid = currentUid
                if (uid == null) return@launch

                val userProfile = repository.getProfile(uid)
                userProfile?.let { profile ->
                    _isDarkMode.value = profile.isDarkMode
                    _themeColor.value = profile.themeColor
                    _fontSize.value = profile.fontSize
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            currentUid?.let { uid ->
                val newMode = !_isDarkMode.value
                repository.updateDarkMode(uid, newMode)
                _isDarkMode.value = newMode
            }
        }
    }

    fun changeThemeColor(color: String) {
        viewModelScope.launch {
            currentUid?.let { uid ->
                repository.updateThemeColor(uid, color)
                _themeColor.value = color
            }
        }
    }

    fun changeFontSize(size: String) {
        viewModelScope.launch {
            currentUid?.let { uid ->
                repository.updateFontSize(uid, size)
                _fontSize.value = size
            }
        }
    }
}
