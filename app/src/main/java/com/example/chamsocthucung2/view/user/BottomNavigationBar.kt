//package com.example.chamsocthucung2.view.user
//
//import androidx.compose.material3.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.navigation.NavController
//import androidx.navigation.compose.currentBackStackEntryAsState
//
//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    val items = listOf(
//        NavItem("Home", "home_screen", Icons.Default.Home),
//        NavItem("Lịch", "appointment_detail_screen", Icons.Default.CalendarToday),
//        NavItem("Hồ sơ", "pet_profiledetail", Icons.Default.Person),
//        NavItem("Chat", "chat_screen", Icons.Default.Chat),
//        NavItem("Tài khoản", "profile_screen", Icons.Default.AccountBox)
//    )
//
//    NavigationBar(containerColor = Color(0xFFFF9800)) {
//        items.forEach { item ->
//            NavigationBarItem(
//                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
//                label = { Text(item.label) },
//                selected = currentRoute == item.route,
//                onClick = {
//                    if (currentRoute != item.route) {
//                        navController.navigate(item.route) {
//                            popUpTo(navController.graph.startDestinationRoute ?: "") {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color.Yellow,
//                    unselectedIconColor = Color.DarkGray,
//                    selectedTextColor = Color.Yellow,
//                    unselectedTextColor = Color.DarkGray
//                )
//            )
//        }
//    }
//}
//
//data class NavItem(
//    val label: String,
//    val route: String,
//    val icon: ImageVector
//)
