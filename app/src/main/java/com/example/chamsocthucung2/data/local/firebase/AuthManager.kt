package com.example.chamsocthucung2.data.local.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun isLogged(): Boolean {
        return auth.currentUser != null
    }

    suspend fun getUserRoleSuspend(): String {
        val uid = auth.currentUser?.uid ?: return "unknown"
        return try {
            val doc = firestore.collection("doctors").document(uid).get().await()
            if (doc.exists()) "doctor" else "user"
        } catch (e: Exception) {
            "unknown"
        }
    }


}
