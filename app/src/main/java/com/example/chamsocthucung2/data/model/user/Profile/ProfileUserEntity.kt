package com.example.chamsocthucung2.data.model.user.Profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_user")
data class ProfileUserEntity(
    @PrimaryKey val uid: String,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val gender: String = "",
    val role: String = "",
    val avatarUrl: String = "",
    val isDarkMode: Boolean = false,
    val themeColor: String = "Cam",
    val fontSize: String = "Trung bình"
)
