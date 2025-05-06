package com.example.chamsocthucung2.data.repository.user

import android.net.Uri
import android.util.Log
import com.example.chamsocthucung2.viewmodel.user.ProfileViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class UserProfile(
    val name: String?,
    val email: String?,
    val avatarUrl: String?
)

interface UserRepository {
    suspend fun getUserFromGoogle(): UserProfile?
    suspend fun getUserFromFirestore(): UserProfile?
    fun getCurrentUserInfo(): FirebaseUser?
    suspend fun reauthenticateUser(currentPassword: String): Boolean
    suspend fun updatePassword(newPassword: String): Boolean

}

class ProfileRepositoryImpl : UserRepository {

    override suspend fun getUserFromGoogle(): UserProfile? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.let {
            UserProfile(
                name = it.displayName,
                email = it.email,
                avatarUrl = it.photoUrl?.toString()
            )
        }
    }

    override suspend fun getUserFromFirestore(): UserProfile? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        val snapshot = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .await()

        return if (snapshot.exists()) {
            UserProfile(
                name = snapshot.getString("fullName"),
                email = snapshot.getString("email"),
                avatarUrl = snapshot.getString("avatarUrl")
            )
        } else {
            null
        }
    }

    //get thông tin user từ firebase Auth
    override fun getCurrentUserInfo(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    // ✅ Reauthenticate trước khi đổi mật khẩu
    override suspend fun reauthenticateUser(currentPassword: String): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        return if (user != null && email != null) {
            try {
                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                user.reauthenticate(credential).await()
                true
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Error loading user profile", e) // <-- thêm dòng này
                false
            }
        } else {
            false
        }
    }

    // ✅ Đổi mật khẩu sau khi đã xác thực lại
    override suspend fun updatePassword(newPassword: String): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user != null) {
            try {
                user.updatePassword(newPassword).await()
                true
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Error loading user profile", e) // <-- thêm dòng này
                false
            }
        } else {
            false
        }
    }
    //cập nhật tên và avata
    class UserRepository {

        private val storage = FirebaseStorage.getInstance().reference
        private val firestore = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()

        suspend fun uploadAvatar(imageUri: Uri): String {
            val avatarRef = storage.child("avatars/${UUID.randomUUID()}.jpg")
            avatarRef.putFile(imageUri).await() // upload file
            return avatarRef.downloadUrl.await().toString() // lấy URL
        }

        suspend fun updateAvatarUrl(url: String) {
            val userId = auth.currentUser?.uid ?: throw Exception("User chưa đăng nhập")
            firestore.collection("users").document(userId)
                .update("avatarUrlUpdate", url)
                .await()
        }
    }


}
