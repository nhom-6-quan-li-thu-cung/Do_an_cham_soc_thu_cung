package com.example.chamsocthucung2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chamsocthucung2.ui.screens.*
import com.example.chamsocthucung2.view.login.*
import com.example.chamsocthucung2.viewmodel.user.PetViewModel
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val MAIN = "main_screen"
    const val REGISTER = "Register_Screen"
    const val FORGOT_PASSWORD = "Forgot_Password_Screen"
    const val CONFIRM_NEW_PASSWORD = "confirm_new_password_screen"
    const val VERIFY_PASSWORD = "verify_password_screen"
    const val HOME = "home_screen"
    const val ACCOUNT = "account_screen"
    const val PET_INFO = "pet_info"
    const val PET_PROFILE_DETAIL = "pet_profiledetail"
    const val TAO_HO_SO = "tao_ho_so"
    const val BOOKING = "booking_screen"
    const val APPOINTMENT_DETAIL = "appointment_detail_screen"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    mAuth: FirebaseAuth,
    googleSignIn: () -> Unit,
    onSignSuccess:() -> Unit,
    signOut: () -> Unit,
    changeTheme: (Color) -> Unit  //
) {
    val petViewModel: PetViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.MAIN) {
        // 🔹 Màn hình xác thực
        composable(Routes.MAIN) { MainScreen(navController, mAuth, googleSignIn, onSignSuccess =onSignSuccess ) }
        composable(Routes.REGISTER) { RegisterScreen(navController) }
        composable(Routes.FORGOT_PASSWORD) { ForgotPasswordScreen(navController) }
        composable(Routes.CONFIRM_NEW_PASSWORD) { ConfirmNewPasswordScreen(navController) }
        composable (Routes.VERIFY_PASSWORD){ VerifyPasswordScreen()}

        // 🔹 Màn hình chính
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.ACCOUNT) { AccountScreen(navController, mAuth, signOut, changeTheme) }

        // 🔹 Màn hình thông tin thú cưng
        composable(Routes.TAO_HO_SO) { TaoHoSoScreen(navController) }
        composable(Routes.PET_INFO) { PetInfoScreen(navController) }
        composable(Routes.PET_PROFILE_DETAIL) { PetProfileDetailScreen(navController) }

        composable(Routes.APPOINTMENT_DETAIL) { // Thêm route này
            AppointmentDetailScreen(navController, petViewModel) // Truyền navController nếu cần
        }

        // 🔹 Màn hình đặt lịch và chi tiết cuộc hẹn (dùng chung ViewModel)
        composable(Routes.BOOKING) { BookingScreen(navController, petViewModel) }
        composable(Routes.APPOINTMENT_DETAIL) { AppointmentDetailScreen(navController, petViewModel) }
    }
}
