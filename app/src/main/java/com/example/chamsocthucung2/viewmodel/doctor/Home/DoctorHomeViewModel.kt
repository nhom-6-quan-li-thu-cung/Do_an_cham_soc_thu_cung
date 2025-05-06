package com.example.chamsocthucung2.viewmodel.doctor.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.viewmodel.user.Appointment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class DoctorHomeViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val appointmentsCollection = firestore.collection("appointments")

    private val _doctorAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    val doctorAppointments: StateFlow<List<Appointment>> = _doctorAppointments

    fun getDoctorAppointments() {
        val doctorId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("appointments")
                    .whereEqualTo("doctorId", doctorId)
                    .get()
                    .await()

                val appointments = snapshot.documents.mapNotNull { doc ->
                    val appointment = doc.toObject(Appointment::class.java)
                    appointment?.copy(id = doc.id) // thêm id để có thể cập nhật
                }

                _doctorAppointments.value = appointments
            } catch (e: Exception) {
                Log.e("DoctorHomeViewModel", "Lỗi khi lấy lịch hẹn cho bác sĩ", e)
            }
        }
    }

    fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                appointmentsCollection.document(appointmentId)
                    .update("status", newStatus)
                    .await()
                getDoctorAppointments()
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Lỗi khi cập nhật trạng thái lịch hẹn", e)
            }
        }
    }
}
