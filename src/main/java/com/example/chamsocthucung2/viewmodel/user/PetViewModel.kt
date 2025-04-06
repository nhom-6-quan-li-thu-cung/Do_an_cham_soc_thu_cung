package com.example.chamsocthucung2.viewmodel.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Dữ liệu lịch hẹn
data class Appointment(
    val date: String,
    val time: String,
    val petName: String,
    val note: String,
    val petImage: String? = null,
    val userId: String? = FirebaseAuth.getInstance().currentUser?.uid // Thêm userId
)
// du lieu thu cung
data class ProfileInfo(
    var petName: String? = "",
    var petType: String? = "",
    var petFeature: String? = "",
    var petWeight: String? = "",
    var petAge: String? = "",
    var ownerName: String? = "",
    var email: String? = "",
    var phone: String? = ""
)

class PetViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val petsCollection = firestore.collection("pets") // Tên collection lưu hồ sơ thú cưng
    private val appointmentsCollection = firestore.collection("appointments") // Tên collection lưu lịch hẹn

    private val _profileInfo = MutableStateFlow(ProfileInfo())
    val profileInfo: StateFlow<ProfileInfo> = _profileInfo

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    // Lưu thông tin thú cưng lên Firestore
    fun savePetInfoToFirestore(petProfile: ProfileInfo) {
        viewModelScope.launch {
            try {
                petsCollection.document(petProfile.email ?: "default_id") // Sử dụng email làm ID
                    .set(petProfile)
                    .await()
                Log.d("Firestore", "Hồ sơ thú cưng đã được lưu lên Firestore với ID: ${petProfile.email}")
            } catch (e: Exception) {
                Log.w("Firestore", "Lỗi khi lưu hồ sơ thú cưng lên Firestore", e)
            }
        }
    }

    // Hàm lấy thông tin thú cưng từ Firestore
    fun getPetInfo(userEmail: String) {
        viewModelScope.launch {
            try {
                val document = petsCollection.document(userEmail).get().await()
                if (document.exists()) {
                    val profile = document.toObject(ProfileInfo::class.java)
                    profile?.let {
                        _profileInfo.value = it
                        Log.d("Firestore", "Dữ liệu hồ sơ thú cưng đã được lấy từ Firestore: ${document.data}")
                    }
                } else {
                    Log.d("Firestore", "Không tìm thấy hồ sơ thú cưng với email: $userEmail trên Firestore")
                    _profileInfo.value = ProfileInfo()
                }
            } catch (e: Exception) {
                Log.d("Firestore", "Lỗi khi lấy hồ sơ thú cưng từ Firestore", e)
            }
        }
    }

    // Cập nhật thông tin ProfileInfo trực tiếp trong ViewModel (lưu tạm thời trên app)
    fun setProfileInfo(petInfo: ProfileInfo) {
        _profileInfo.value = petInfo
        Log.d("PetViewModel", "ProfileInfo updated in ViewModel (local): ${_profileInfo.value}")
    }

    // Lưu thông tin thú cưng ngay trên app (chỉ lưu trong bộ nhớ của ViewModel)
    fun savePetInfoLocally(
        name: String, type: String, feature: String, weight: String, age: String,
        owner: String, mail: String, sdt: String
    ) {
        val petProfile = ProfileInfo(name, type, feature, weight, age, owner, mail, sdt)
        _profileInfo.value = petProfile
        Log.d("PetViewModel", "Hồ sơ thú cưng đã được lưu cục bộ (trong ViewModel): $petProfile")
    }

    // Lưu lịch hẹn lên Firestore
    fun saveAppointment(date: String, time: String, note: String) {
        val currentPetName = _profileInfo.value.petName ?: "" // Vẫn lấy tên thú cưng (có thể rỗng)
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            val newAppointment = Appointment(
                petName = currentPetName, // Tên thú cưng có thể rỗng ở đây
                date = date,
                time = time,
                note = note,
                userId = userId
            )
            viewModelScope.launch {
                Log.d("PetViewModel", "Bắt đầu lưu lịch hẹn lên Firestore: $newAppointment")
                try {
                    appointmentsCollection.add(newAppointment).await()
                    _appointments.update { currentAppointments -> currentAppointments + newAppointment }
                    Log.d("Firestore", "Lịch hẹn đã được lưu lên Firestore cho user $userId và thú cưng $currentPetName")
                } catch (e: Exception) {
                    Log.w("Firestore", "Lỗi khi lưu lịch hẹn lên Firestore", e)
                }
            }
        } ?: run {
            Log.w("PetViewModel", "Không thể lưu lịch hẹn vì chưa đăng nhập.")
        }
    }

    // Hàm để lấy lịch hẹn từ Firestore (bạn sẽ cần triển khai hàm này)
    fun getAppointments() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                try {
                    appointmentsCollection
                        .whereEqualTo("userId", userId)
                        .get()
                        .await()
                        .documents.mapNotNull { it.toObject(Appointment::class.java) }
                        .let { fetchedAppointments ->
                            _appointments.value = fetchedAppointments
                            Log.d("Firestore", "Đã lấy ${fetchedAppointments.size} lịch hẹn từ Firestore cho user $userId")
                        }
                } catch (e: Exception) {
                    Log.w("Firestore", "Lỗi khi lấy lịch hẹn từ Firestore", e)
                }
            }
        } ?: run {
            Log.w("PetViewModel", "Không thể lấy lịch hẹn vì chưa đăng nhập.")
        }
    }

    // Hàm để cập nhật danh sách lịch hẹn cục bộ (nếu cần)
    fun updateAppointmentsLocally(newAppointments: List<Appointment>) {
        _appointments.value = newAppointments
    }
}