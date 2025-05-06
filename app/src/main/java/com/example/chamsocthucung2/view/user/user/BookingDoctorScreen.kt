package com.example.chamsocthucung2.view.user.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chamsocthucung2.data.repository.doctor.dulieu.DoctorInfo
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.viewmodel.doctor.dulieu.DoctorInfoViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookingDoctorScreen(
    navController: NavController,
    viewModel: DoctorInfoViewModel = viewModel()
) {
    val firestore = FirebaseFirestore.getInstance()
    val doctors = remember { mutableStateOf<Map<String, DoctorInfo>>(emptyMap()) }

    LaunchedEffect(Unit) {
        firestore.collection("doctorinfo").get()
            .addOnSuccessListener { result ->
                val list = result.documents.associate { doc ->
                    doc.id to doc.toObject(DoctorInfo::class.java)!!.copy(id = doc.id)
                }
                doctors.value = list
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Chọn bác sĩ", style = MaterialTheme.typography.titleLarge)

        doctors.value.forEach { (_, doctor) ->
            ExpandableDoctorCard(doctor = doctor, onBookingClick = {
                navController.navigate(Routes.BOOKING)
            })
        }
    }
}

@Composable
fun ExpandableDoctorCard(
    doctor: DoctorInfo,
    onBookingClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = doctor.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Chuyên môn: ${doctor.mainSpecialty}")

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    doctor.age.let { Text("Tuổi: $it") }
                    doctor.email?.let { Text("Email: $it") }
                    doctor.phone.let { Text("SĐT: $it") }
                    doctor.hospital?.let { Text("Bệnh viện: $it") }
                    if (doctor.specialties.isNotEmpty()) {
                        Text("Các chuyên môn khác: ${doctor.specialties.joinToString(", ")}")
                    }
                    doctor.experienceYears?.let { Text("Kinh nghiệm: $it năm") }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onBookingClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Đặt lịch khám")
                    }
                }
            }
        }
    }
}
