package com.example.chamsocthucung2.data.model.login

import com.example.chamsocthucung2.view.login.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)