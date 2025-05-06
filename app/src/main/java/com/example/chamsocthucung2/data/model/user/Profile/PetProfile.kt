package com.example.chamsocthucung2.data.model.user.Profile

data class PetProfile(
    val id: String,
    val name: String,
    val type: String,
    val age: Int,
//    val weight: Double,
//    val healthStatus: Int, // 0-100%
    val healthPercentage: Int,
    val avatarUrl: String,
    val species: String
//    val isPremium: Boolean = false
)