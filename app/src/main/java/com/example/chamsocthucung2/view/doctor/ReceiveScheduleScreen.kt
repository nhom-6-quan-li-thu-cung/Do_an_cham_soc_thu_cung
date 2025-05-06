package com.example.chamsocthucung2.view.doctor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.chamsocthucung2.viewmodel.doctor.Home.DoctorHomeViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.ui.components.DoctorBottomNavigationBar
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ReceiveScheduleScreen(
    navController: NavController,
    doctorViewModel: DoctorHomeViewModel = viewModel()
)
{
    val appointments by doctorViewModel.doctorAppointments.collectAsState()
    LaunchedEffect(Unit) {
        doctorViewModel.getDoctorAppointments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📥 Lịch hẹn chờ xử lý", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.White
            )
        },
        bottomBar = { DoctorBottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFFFE4B5), Color.White)))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🩺 Lịch hẹn đang chờ xử lý", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                val pendingAppointments = appointments.filter { it.status == "pending" }

                if (pendingAppointments.isEmpty()) {
                    Text("Hiện không có lịch hẹn nào đang chờ.", fontSize = 18.sp, color = Color.Gray)
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(pendingAppointments) { appointment ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
                                backgroundColor = Color.White,
                                elevation = 4.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.cat_dog),
                                            contentDescription = "Hình thú cưng",
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFFA500))
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text("🐶 Tên thú cưng: ${appointment.petName}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                            Text("📅 Ngày: ${appointment.date}", fontSize = 16.sp)
                                            Text("⏰ Giờ: ${appointment.time}", fontSize = 16.sp)
                                            Text(
                                                "📝 Ghi chú: ${if (appointment.note.isEmpty()) "Không có" else appointment.note}",
                                                fontSize = 16.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Button(
                                            onClick = {
                                                doctorViewModel.updateAppointmentStatus(
                                                    appointmentId = appointment.id,
                                                    newStatus = "accepted"
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
                                        ) {
                                            Text("✔️ Chấp nhận", color = Color.White)
                                        }
                                        Button(
                                            onClick = {
                                                doctorViewModel.updateAppointmentStatus(
                                                    appointmentId = appointment.id,
                                                    newStatus = "rejected"
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF44336))
                                        ) {
                                            Text("❌ Từ chối", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

