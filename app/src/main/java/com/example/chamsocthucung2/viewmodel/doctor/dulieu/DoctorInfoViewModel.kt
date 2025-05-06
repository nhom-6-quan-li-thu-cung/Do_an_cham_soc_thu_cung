package com.example.chamsocthucung2.viewmodel.doctor.dulieu

import androidx.lifecycle.ViewModel
import com.example.chamsocthucung2.data.repository.doctor.dulieu.DoctorInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow

class DoctorInfoViewModel : ViewModel() {
    val doctorInfo = MutableStateFlow(DoctorInfo()) // <-- phải có khởi tạo

    val isFormValid: Boolean
        get() = with(doctorInfo.value) {
            name.isNotBlank() &&
                    age > 0 &&
                    email.isNotBlank() &&
                    phone.isNotBlank() &&
                    hospital.isNotBlank() &&
                    specialty.isNotBlank() &&
                    experienceYears >= 0
        }

    fun updateInfo(newInfo: DoctorInfo) {
        doctorInfo.value = newInfo
    }

    fun saveToFirestore(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("doctorinfo")
                .document(uid)
                .set(doctorInfo.value)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onError(it) }
        } else {
            onError(Exception("Người dùng chưa đăng nhập"))
        }
    }
}
