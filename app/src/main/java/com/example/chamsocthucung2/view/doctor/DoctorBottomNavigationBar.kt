package com.example.chamsocthucung2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chamsocthucung2.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorBottomNavigationBar(navController: NavController) {
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = Color(0xFFFF9800),
        contentColor = Color(0xFF424242)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround // Đã sửa ở đây
        ) {
            BottomNavItem(icon = Icons.Outlined.Home, label = "Trang chủ", onClick = { navController.navigate(Routes.HOMEDOCTOR) })
            BottomNavItem(icon = Icons.Outlined.CalendarToday, label = "Đặt lịch", onClick = { navController.navigate(Routes.RECEIVE_SCHEDULE) })
            BottomNavItem(icon = Icons.Outlined.FolderOpen, label = "Hồ sơ", onClick = { /* TODO */ })
            BottomNavItem(icon = Icons.Outlined.ChatBubbleOutline, label = "Chat", onClick = { /* TODO */ })
            BottomNavItem(icon = Icons.Outlined.PersonOutline, label = "Tài khoản", onClick = {
                navController.navigate(Routes.PROFILE_DOCTOR)
            })
        }
    }
}

@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp), tint = Color.Gray)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}