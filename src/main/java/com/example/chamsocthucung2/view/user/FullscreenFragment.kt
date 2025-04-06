package com.example.chamsocthucung2.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.ui.components.BottomNavigationBar
import com.example.chamsocthucung2.viewmodel.user.PetViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.Card
import androidx.compose.ui.text.style.TextOverflow
import com.example.chamsocthucung2.navigation.Routes
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    petViewModel: PetViewModel = viewModel(),
    userEmail: String = FirebaseAuth.getInstance().currentUser?.email ?: "abcxyz@gmail.com"
) {
    val profileInfo by petViewModel.profileInfo.collectAsState()

    val doctors = remember {
        listOf(
            VetDoctor("Dr. Bình", "Chuyên gia nội khoa", R.drawable.doctor), // Thay bằng hình ảnh thật
            VetDoctor("Dr. Hoa", "Chuyên gia ngoại khoa", R.drawable.doctor2)  // Thay bằng hình ảnh thật
        )
    }

    Scaffold(
        topBar = { /* ... TopAppBar như hiện tại ... */ },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                // Hero Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image1), // Hình ảnh thú cưng vui vẻ
                            contentDescription = "Chào mừng",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Chào mừng,", style = MaterialTheme.typography.headlineSmall)
                        Text(userEmail.substringBefore("@"), style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate(Routes.BOOKING) },
                            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFFFFA500), disabledContentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Đặt lịch khám ngay")
                        }
                    }
                }

                // Hồ sơ thú cưng
                Card(
                    onClick = { navController.navigate(Routes.PET_INFO) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img), // Icon hồ sơ mặc định
                            contentDescription = "Pet Avatar Placeholder",
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE0E0E0)),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                profileInfo.petName.takeIf { it?.isNotEmpty() == true } ?: "Chưa có hồ sơ",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                profileInfo.petType.takeIf { it?.isNotEmpty() == true } ?: "Thêm thông tin thú cưng",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Xem hồ sơ")
                    }
                }

                Text(
                    "Dịch vụ Thú Cưng",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Dịch vụ dạng lưới
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(services) { service ->
                        ServiceCard(service, navController)
                    }
                }

                Text(
                    "Đội Ngũ Bác Sĩ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Bác sĩ dạng hàng ngang có thể cuộn
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(doctors) { doctor ->
                        DoctorCard(doctor, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCard(service: Service, navController: NavController) {
    Card(
        onClick = { navController.navigate(service.route) },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Để tạo hình vuông
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFFFF3E0), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = service.icon),
                    contentDescription = service.name,
                    modifier = Modifier.size(36.dp),
                    tint = Color(0xFFFFA500)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                service.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DoctorCard(doctor: VetDoctor, navController: NavController) {
    Card(
        onClick = { /* Điều hướng đến chi tiết bác sĩ nếu muốn */ },
        modifier = Modifier
            .width(180.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = doctor.imageRes),
                contentDescription = "Doctor",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(doctor.name, style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center)
            Text(doctor.specialty, style = MaterialTheme.typography.bodySmall, color = Color.Gray, textAlign = TextAlign.Center)
            Button(
                onClick = { navController.navigate(Routes.BOOKING) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(contentColor = Color(0xFFFFA500), disabledContentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Đặt lịch")
            }
        }
    }
}

data class PetInfo(val name: String, val breed: String, val imageRes: Int)
data class VetDoctor(val name: String, val specialty: String, val imageRes: Int)
data class Service(val name: String, val icon: Int, val route: String)

val services = listOf(
    Service("Tư vấn sức khỏe", R.drawable.ic_yte, "medical"),
    Service("Chăm sóc đặc biệt", R.drawable.ic_yte, "care"),
    Service("Spa & Vệ sinh", R.drawable.ic_yte, "grooming"),
    Service("Khám tổng quát", R.drawable.ic_yte, "checkup")
)
@Composable
fun TaoHoSoScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(text = "Xin chào,", fontSize = 14.sp, color = Color.Gray)
                        Text(text = "abcxyz.com", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { /* Xử lý thông báo */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_thongbao),
                            contentDescription = "Thông báo",
                            tint = Color.Black
                        )
                    }
                }
            }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.backround),
                contentDescription = "Ảnh nền",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))


                Button(
                    onClick = { navController.navigate("pet_info") },
                    modifier = Modifier
                        .width(250.dp)
                        .height(48.dp)
                        .offset(x = 30.dp, y = 370.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500))
                ) {
                    Text(
                        text = "+ Thêm hồ sơ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(400.dp))

                Text(
                    text = "Chưa có hồ sơ thú cưng",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Bạn chưa tạo lập hồ sơ thú cưng.\nẤn Tiếp tục để cập nhật thông tin nhé!",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }
    }
}
@Composable
fun PetInfoScreen(navController: NavController, petViewModel: PetViewModel = viewModel()) {
    var petName by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("") }
    var petFeature by remember { mutableStateOf("") }
    var petWeight by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val context = LocalContext.current // Get context for Toast

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nhập thông tin thú cưng", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                backgroundColor = Color.White,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text("Chọn loại thú cưng", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                PetTypeOption("Chó", R.drawable.cat_dog, petType) { petType = "Chó" }
                PetTypeOption("Mèo", R.drawable.cat_dog, petType) { petType = "Mèo" }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                backgroundColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PetInputField(label = "Tên thú cưng", value = petName, onValueChange = { petName = it })
                    PetInputField(label = "Đặc điểm", value = petFeature, onValueChange = { petFeature = it })
                    PetInputField(label = "Cân nặng (kg)", value = petWeight, onValueChange = { petWeight = it })
                    PetInputField(label = "Tuổi", value = petAge, onValueChange = { petAge = it })
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Thông tin chủ nhân", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            PetInputField(label = "Họ tên", value = ownerName, onValueChange = { ownerName = it })
            PetInputField(label = "Email", value = email, onValueChange = { email = it })
            PetInputField(label = "SĐT", value = phone, onValueChange = { phone = it })

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    Log.d("PetInfoScreen", "PetInfo to save: petInfo")
                    Log.d("PetInfoScreen", "PetInfo in ViewModel after save: ${petViewModel.profileInfo.value}")
                    if (email.isNotEmpty()) {
                        petViewModel.savePetInfoLocally(
                            name = petName,
                            type = petType,
                            feature = petFeature,
                            weight = petWeight,
                            age = petAge,
                            owner = ownerName,
                            mail = email,
                            sdt = phone
                        )
                        Toast.makeText(context, "Thông tin đã được lưu!", Toast.LENGTH_SHORT).show()
                        navController.navigate("pet_profiledetail")
                    } else {
                        Toast.makeText(context, "Vui lòng nhập email để lưu hồ sơ.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(6.dp, shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500))
            ) {
                Text("Lưu và xem hồ sơ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}
@Composable
fun PetTypeOption(label: String, iconRes: Int, selectedType: String, onSelect: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selectedType == label) Color(0xFFFFC107) else Color.LightGray) // 🌟 Vàng khi chọn
            .padding(12.dp)
            .clickable { onSelect() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (selectedType == label) Color.Black else Color.DarkGray
        )
    }
}


@Composable
fun PetInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}


@Composable
fun PetProfileDetailScreen(navController: NavController, petViewModel: PetViewModel = viewModel()) {
    val profileInfo by petViewModel.profileInfo.collectAsState()
    Log.d("PetDetailScreen", "ProfileInfo in Detail Screen: $profileInfo")
    val userEmail = FirebaseAuth.getInstance().currentUser?.email
    Log.d("PetProfileDetailScreen", "User Email: $userEmail") // Thêm dòng này

    Log.d("PetProfileDetailScreen", "Composed with ProfileInfo: $profileInfo")
    LaunchedEffect(userEmail) {
        if (userEmail != null) {
            Log.d("PetProfileDetailScreen", "Calling getPetInfo with email: $userEmail") // Thêm dòng này
            petViewModel.getPetInfo(userEmail)
        } else {
            Log.w("PetProfileDetailScreen", "Không có email người dùng để lấy hồ sơ.")
        }
    }
    LaunchedEffect(profileInfo) {
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ thú cưng", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                backgroundColor = Color.White,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 🔹 Hình nền
            Image(
                painter = painterResource(id = R.drawable.image4),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.image1),
                    contentDescription = "Pet Image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(4.dp)
                )
                Text(
                    text = profileInfo.petName ?: "Chưa có tên",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = profileInfo.petType ?: "Chưa rõ loại",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))
                CardInfoSection(
                    title = "Thông tin thú cưng",
                    data = listOf(
                        "Đặc điểm" to (profileInfo.petFeature ?: ""),
                        "Cân nặng" to (profileInfo.petWeight?.let { "$it kg" } ?: ""),
                        "Tuổi" to (profileInfo.petAge?.let { "$it tuổi" } ?: "")
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                CardInfoSection(
                    title = "Người chăm sóc",
                    data = listOf(
                        "Họ tên" to (profileInfo.ownerName ?: ""),
                        "Email" to (profileInfo.email ?: ""),
                        "SĐT" to (profileInfo.phone ?: "")
                    )
                )

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navController.navigate("booking_screen") }, // Chuyển sang màn hình đặt lịch
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500))
                ) {
                    Text("Hoàn tất hồ sơ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
@Composable
fun CardInfoSection(title: String, data: List<Pair<String, String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF444444))
            Spacer(modifier = Modifier.height(10.dp))
            data.forEach { (label, value) ->
                InfoRow(label, value)
            }
        }
    }
}
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
@Composable
fun BookingScreen(navController: NavController, petViewModel: PetViewModel = viewModel()) {
    Log.d("BookingScreen", "Tên thú cưng khi BookingScreen được tạo: ${petViewModel.profileInfo.value.petName}") // Thêm log này
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.image1),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Đặt lịch khám", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Quay lại")
                        }
                    },
                    backgroundColor = Color(0xFFFFC107),
                    elevation = 8.dp
                )
            },

            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text("Chọn ngày khám", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth -> selectedDate = "$dayOfMonth/${month + 1}/$year" },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp,
                    backgroundColor = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(id = R.drawable.ic_home), contentDescription = null, tint = Color(0xFFFFC107))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = if (selectedDate.isEmpty()) "Chọn ngày" else selectedDate, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Chọn giờ khám", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hour, minute -> selectedTime = "%02d:%02d".format(hour, minute) },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp,
                    backgroundColor = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(id = R.drawable.ic_home), contentDescription = null, tint = Color(0xFFFFC107))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = if (selectedTime.isEmpty()) "Chọn giờ" else selectedTime, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú thêm") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                            Log.d("BookingScreen", "Tên thú cưng trước khi lưu lịch: ${petViewModel.profileInfo.value.petName}") // Thêm log này
                            petViewModel.saveAppointment(selectedDate, selectedTime, note)
                            Toast.makeText(context, "Lịch đã được đặt!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.APPOINTMENT_DETAIL)
                        } else {
                            Toast.makeText(context, "Vui lòng chọn ngày, giờ", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC107)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    elevation = ButtonDefaults.elevation(6.dp)
                ) {
                    Text("Xác nhận đặt lịch", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



@Composable
fun DateTimePickerButton(label: String, selectedValue: String, iconRes: Int, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White.copy(alpha = 0.8f)) // Nền trắng mờ
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = Color(0xFFFFA500) // Màu cam
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = if (selectedValue.isEmpty()) label else selectedValue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
@Composable
fun CustomOutlinedTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White.copy(alpha = 0.8f),
            focusedBorderColor = Color(0xFFFFA500),
            unfocusedBorderColor = Color.Gray
        )
    )
}
@Composable
fun AppointmentDetailScreen(
    navController: NavController,
    petViewModel: PetViewModel = viewModel()
) {
    val appointments by petViewModel.appointments.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📅 Lịch hẹn đã đặt", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Quay lại")
                    }
                },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.White
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFFFE4B5), Color.White))) // 🌟 Hình nền gradient
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🗓 Danh sách lịch hẹn", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (appointments.isEmpty()) {
                    Text("Không có lịch hẹn nào.", fontSize = 18.sp, color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(appointments) { appointment ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
                                backgroundColor = Color.White,
                                elevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val petImage = appointment.petImage ?: R.drawable.cat_dog
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
                                        Text(
                                            "🐶 Tên thú cưng: ${appointment.petName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                        Text("📅 Ngày: ${appointment.date}", fontSize = 16.sp)
                                        Text("⏰ Giờ: ${appointment.time}", fontSize = 16.sp)
                                        Text(
                                            "📝 Ghi chú: ${if (appointment.note.isEmpty()) "Không có" else appointment.note}",
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )
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
@Composable
fun AccountScreen(
    navController: NavController,
    mAuth: FirebaseAuth,
    googleSignIn: () -> Unit,
    changeTheme: (Color) -> Unit
) {
    val user = mAuth.currentUser
    val context = LocalContext.current
    var selectedColor by remember { mutableStateOf(Color(0xFFF8E7C0)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tài khoản", fontWeight = FontWeight.Bold) },
                backgroundColor = Color(0xFFFF9800),
                contentColor = Color.White
            )
        },
        backgroundColor = selectedColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔵 Avatar
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(Color.LightGray, shape = CircleShape)
                    .border(4.dp, Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(user?.photoUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🟢 Hiển thị tên & email
            Text(
                text = user?.displayName ?: "Chưa có tên",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Text(
                text = user?.email ?: "Không có email",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    googleSignIn()
                    mAuth.signOut()
                    navController.navigate("main_screen") {
                        popUpTo("main_screen") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Đăng xuất", color = Color.White, fontWeight = FontWeight.Bold)
            }

        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar (
        containerColor = Color.White
    ) {
        val items = listOf(
            "Home" to R.drawable.ic_home,
            "Lịch" to R.drawable.ic_chat,
            "Chat" to R.drawable.ic_chat,
            "Tài khoản" to R.drawable.ic_profile
        )

        items.forEach { (label, icon) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = label
                    )
                },
                label = { Text(label) },
                selected = false,
                onClick = { /* Xử lý khi nhấn */ }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewHomScreen() {
    val navController = rememberNavController()
    val petViewModel = PetViewModel()

    HomeScreen(navController = navController, petViewModel = petViewModel)
}
@Preview(showBackground = true)
@Composable
fun TaoHoSoScreen() {
    val navController = rememberNavController()
    TaoHoSoScreen(navController)
}
@Preview(showBackground = true)
@Composable
fun PreviewPetInfoScreen() {
    val navController = rememberNavController()
    val petViewModel = PetViewModel()

    PetInfoScreen(navController = navController, petViewModel = petViewModel)
}
@Preview(showBackground = true)
@Composable
fun PreviewPetProfileDetailScreen() {
    val navController = rememberNavController()
    val petViewModel = PetViewModel()

    PetProfileDetailScreen(navController = navController, petViewModel = petViewModel)
}
@Preview(showBackground = true)
@Composable
fun PreviewBookingScreen() {
    val navController = rememberNavController()

    BookingScreen(navController = navController)
}
@Preview(showBackground = true)
@Composable
fun PreviewAppointmentDetailScreen() {
    val navController = rememberNavController()

    BookingScreen(navController = navController)
}
