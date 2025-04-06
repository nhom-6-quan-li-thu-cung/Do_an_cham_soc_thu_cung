package com.example.chamsocthucung2.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Color(0xFFFF9800), // 🎨 Màu vàng
        contentColor = Color.Black // Màu chữ và icon
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate("home_screen") },
            selectedContentColor = Color.Black, // Giữ màu icon khi được chọn
            unselectedContentColor = Color.DarkGray // Màu icon khi không được chọn
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Đặt lịch") },
            label = { Text("Đặt lịch") },
            selected = false,
            onClick = { navController.navigate("appointment_detail_screen") },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.DarkGray
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Hồ sơ") },
            label = { Text("Hồ sơ") },
            selected = false,
            onClick = { navController.navigate("pet_profiledetail") },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.DarkGray
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
            label = { Text("Chat") },
            selected = false,
            onClick = { /* Chuyển sang màn hình chat */ },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.DarkGray
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.AccountBox, contentDescription = "Tài khoản") },
            label = { Text("Tài khoản") },
            selected = false,
            onClick = { navController.navigate("account_screen") }, // ✅ Chuyển sang màn hình tài khoản
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.DarkGray
        )

    }
}


