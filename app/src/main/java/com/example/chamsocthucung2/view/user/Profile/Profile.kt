package com.example.chamsocthucung2.view.user.Profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.data.repository.user.ProfileRepositoryImpl
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.view.user.user.BottomNavigationBar
import com.example.chamsocthucung2.viewmodel.login.LoginViewModel
import com.example.chamsocthucung2.viewmodel.user.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    navController: NavController
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFE4B5)
    val uiState by viewModel.userProfileState.collectAsState()

    // Chỉ chạy một lần khi Composable được tạo
    LaunchedEffect(true) {
        viewModel.loadGoogleUser()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        backgroundColor = backgroundColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is ProfileUiState.Loading -> LoadingIndicator()
                is ProfileUiState.Success -> {
                    val pet = (uiState as ProfileUiState.Success).pet
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PetHeader()
                        SettingOptionsSection(
                            onOptionClick = { option ->
                                when (option) {
                                    "notifications" -> navController.navigate(Routes.NOTIFICATION_SETTINGS)
                                    "user" -> navController.navigate(Routes.USER_TYPE_SELECTION)
                                    "about" -> navController.navigate(Routes.ABOUT_APP)
                                    "customization" -> navController.navigate(Routes.CUSTOMIZATION)
                                }
                            }
                        )
                    }
                }
                is ProfileUiState.Error -> ErrorScreen((uiState as ProfileUiState.Error).message)
            }
        }
    }
}
@Composable
fun CustomizationScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    signOut: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận đăng xuất") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất không?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    loginViewModel.logout()
                    signOut()
                    navController.navigate(Routes.MAIN) {
                        popUpTo(0)
                    }
                    Toast.makeText(context, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Đồng ý")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    TextButton(
        onClick = { showDialog = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Red
        )
    ) {
        Text("Đăng xuất")
    }
}

@Composable
fun PetHeader(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Nền gradient vàng cam
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp) // tăng thêm để chứa info dưới avatar
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFA500), Color(0xFFFFFF00))
                    )
                )
        ) {
            // Hàng chứa nút back và tiêu đề
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(3f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        fun saveNewName(
            name: String,
            viewModel: ProfileViewModel,
            onDone: () -> Unit
        ) {
            if (name.isBlank()) return  // Không lưu tên trống

            viewModel.updateUserfullName(name)  // Gọi ViewModel update
            onDone()  // Đóng chế độ chỉnh sửa
        }
        val fullName by viewModel.userName.observeAsState()
        val email by viewModel.userEmail.observeAsState()
        val avatarUrl by viewModel.avatarUrl.observeAsState("")
        //cập nhật tên mới
        var isEditingName by remember { mutableStateOf(false) }
        var newName by remember { mutableStateOf(fullName ?: "") }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            if (fullName.isNullOrBlank() || email.isNullOrBlank()) {
                viewModel.loadUserInfo()
            } else {
                viewModel.loadFirestoreUser()
            }
        }

        val userViewModel = remember { ProfileViewModel.UserViewModel(ProfileRepositoryImpl.UserRepository()) }
        val context = LocalContext.current
        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                userViewModel.uploadAndSaveAvatar(it)
            }
        }
        // Nội dung avatar + tên/email + nút chỉnh sửa
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        pickImageLauncher.launch("image/*")
                        viewModel.loadFirestoreUser()
                        Toast.makeText(context, "Chọn ảnh đại diện", Toast.LENGTH_SHORT).show()
                    }
            ) {
                val avatarPainter = rememberAsyncImagePainter(
                    model = avatarUrl ?: R.drawable.default_avatar
                )
                Image(
                    painter = avatarPainter,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Chỉnh avatar",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(24.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 1.dp)
                    .fillMaxWidth()
            ) {
                if (isEditingName) {
                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        textStyle = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    // Mất focus thì tự lưu
                                    coroutineScope.launch {
                                        saveNewName(newName, viewModel, {
                                            isEditingName = false
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Đã cập nhật tên!")
                                            }
                                        })
                                    }
                                }
                            },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        placeholder = { Text("Nhập tên mới") }
                    )

                    // Tự động focus khi vào chế độ chỉnh sửa
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                } else {
                    Text(
                        text = (fullName ?: ""),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                    )
                }

                IconButton(onClick = {
                    isEditingName = true
                    newName = fullName ?: ""
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Chỉnh tên",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            Text(
                text = (email ?:""), // hoặc dùng pet.email nếu bạn muốn
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun SettingOptionsSection(
    modifier: Modifier = Modifier,
    onOptionClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ProfileOptionItem("Thông báo", Icons.Default.Notifications, onClick = { onOptionClick("notifications") })
        ProfileOptionItem("Người dùng", Icons.Default.Person, onClick = { onOptionClick("user") })
        ProfileOptionItem("Giới thiệu", Icons.Default.Info, onClick = { onOptionClick("about") })
        ProfileOptionItem("Tùy chỉnh", Icons.Default.Settings, onClick = { onOptionClick("customization") })
    }
}

@Composable
fun NotificationSettingsScreen(
    navController: NavController
) {
    var isNotificationEnabled by remember { mutableStateOf(true) }
    var temporaryMode by remember { mutableStateOf(false) }
    var selectedDuration by remember { mutableStateOf("1 giờ") }

    val durations = listOf("1 giờ", "2 giờ", "3 giờ", "5 giờ", "8 giờ")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt thông báo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.Black
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Thông báo vĩnh viễn", fontWeight = FontWeight.Bold)
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = {
                    isNotificationEnabled = it
                    if (it) temporaryMode = false
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Bật thông báo tạm thời", fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = temporaryMode,
                    onCheckedChange = {
                        temporaryMode = it
                        if (it) isNotificationEnabled = false
                    }
                )
                Text("Bật trong thời gian cụ thể")
            }

            if (temporaryMode) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Chọn thời gian bật:")
                DropdownMenuWithSelection(
                    items = durations,
                    selectedItem = selectedDuration,
                    onItemSelected = { selectedDuration = it }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    // TODO: lưu cài đặt
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lưu cài đặt")
            }
        }
    }
}

sealed class UserType {
    object User : UserType()
    object Doctor : UserType()
}

@Composable
fun UserTypeSelectionScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chọn loại người dùng") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.Black
            )
        }
    ) { padding ->
        // Nội dung chính của màn hình
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề
            Text(
                text = "Chọn loại người dùng",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Tùy chọn User
            Button(
                onClick = { onOptionClick(UserType.User, navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
            ) {
                Text("Người dùng", color = Color.White, fontSize = 18.sp)
            }

            // Tùy chọn Doctor
            Button(
                onClick = { onOptionClick(UserType.Doctor, navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3))
            ) {
                Text("Bác sĩ", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

fun onOptionClick(userType: UserType, navController: NavController) {
    when (userType) {
        UserType.User -> {
            // Điều hướng đến màn hình người dùng
            navController.navigate(Routes.HOME)
        }
        UserType.Doctor -> {
            // Điều hướng đến màn hình nhập điều khoản bác sĩ
            navController.navigate(Routes.DOCTOR_TERMS)
        }
    }
}

@Composable
fun DoctorTermsScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    signOut: () -> Unit
) {
    // Biến trạng thái để kiểm tra xem bác sĩ có đồng ý với điều khoản hay không
    var isAccepted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Điều khoản bác sĩ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.Black
            )
        }
    ) { padding ->
        // Nội dung màn hình điều khoản bác sĩ
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề
            Text(
                text = "Điều khoản bác sĩ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Hiển thị điều khoản bác sĩ
            Text(
                text = "1. Bạn phải có bằng cấp hợp lệ.\n\n2. Bạn phải có ít nhất 2 năm kinh nghiệm trong ngành.\n\n3. Bạn cần tuân thủ các quy định và đạo đức nghề nghiệp.\n\n4. Bạn sẽ tham gia vào hệ thống chẩn đoán và chăm sóc sức khỏe cho thú cưng.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Checkbox đồng ý điều khoản
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isAccepted,
                    onCheckedChange = { isAccepted = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tôi đồng ý với điều khoản bác sĩ")
            }

            Spacer(modifier = Modifier.height(32.dp))
            val context = LocalContext.current
// Nút TIẾP TỤC để đăng xuất và đăng nhập lại
            Button(
                onClick = {
                    if (isAccepted) {
                        loginViewModel.logout()
                        signOut()
                        navController.navigate(Routes.DOCTOR_LOGIN) {
                            popUpTo(0) // Xóa toàn bộ back stack để tránh quay ngược về
                        }
                        Toast.makeText(context, "Đăng xuất thành công. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                enabled = isAccepted,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isAccepted) Color(0xFFFFA500) else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text("Tiếp tục", fontSize = 18.sp)
            }
        }
    }
}
@Composable
fun DropdownMenuWithSelection(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedItem)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun ProfileOptionItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun AboutAppScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giới thiệu") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.Black
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )


            Text(
                text = "PetCare App",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Phiên bản: 1.0.0",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ứng dụng giúp bạn quản lý sức khỏe thú cưng, đặt lịch khám và nhận thông báo từ bác sĩ thú y.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Phát triển bởi: Nhóm 6 UTH ",
                fontSize = 16.sp
            )

            Text(
                text = "Liên hệ: support@petcare.com",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Chính sách bảo mật",
                color = Color.Blue,
                modifier = Modifier
                    .clickable {
                        // Tạm thời toast hoặc chuyển màn hình WebView
                    }
                    .padding(4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun SwitchRow(
    icon: ImageVector,
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text(text, modifier = Modifier.weight(1f))
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun PetLottieAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pet_animation))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever, // chạy mãi mãi
        modifier = Modifier.size(150.dp)
    )
}


@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Đã xảy ra lỗi: $message", color = Color.Red)
    }
}
