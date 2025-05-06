package com.example.chamsocthucung2.data.repository.doctor.login

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DoctorRepository(private val firestore: FirebaseFirestore, application: Application) {

    suspend fun migrateUserToDoctor(uid: String): Result<Unit> {
        return try {
            val userRef = firestore.collection("users").document(uid)
            val doctorRef = firestore.collection("doctors").document(uid)

            val snapshot = userRef.get().await()
            if (!snapshot.exists()) {
                return Result.failure(Exception("Không tìm thấy thông tin người dùng"))
            }

            // Lấy dữ liệu người dùng và cập nhật role thành doctor
            val userData = snapshot.data?.toMutableMap() ?: mutableMapOf()
            userData["role"] = "doctor"

            // Ghi vào doctors và xoá khỏi users
            doctorRef.set(userData).await()
            userRef.delete().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
