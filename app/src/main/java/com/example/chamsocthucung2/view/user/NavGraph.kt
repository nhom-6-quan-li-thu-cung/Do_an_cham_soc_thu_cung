package com.example.chamsocthucung2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chamsocthucung2.data.repository.user.ProfileRepositoryImpl
import com.example.chamsocthucung2.view.doctor.AdviceScreen
import com.example.chamsocthucung2.view.doctor.CareGuideScreen
import com.example.chamsocthucung2.view.doctor.DoctorInfoScreen
import com.example.chamsocthucung2.view.doctor.DoctorLoginScreen
import com.example.chamsocthucung2.view.doctor.HomeDoctorScreen
import com.example.chamsocthucung2.view.doctor.ProfileDoctorScreen
import com.example.chamsocthucung2.view.doctor.ReceiveScheduleScreen
import com.example.chamsocthucung2.view.login.*
import com.example.chamsocthucung2.view.user.AppointmentDetailScreen
import com.example.chamsocthucung2.view.user.BookingScreen
import com.example.chamsocthucung2.view.user.HomeScreen
import com.example.chamsocthucung2.view.user.PetInfoScreen
import com.example.chamsocthucung2.view.user.PetProfileDetailScreen
import com.example.chamsocthucung2.view.user.Profile.AboutAppScreen
import com.example.chamsocthucung2.view.user.Profile.CustomizationScreen
import com.example.chamsocthucung2.view.user.Profile.DoctorTermsScreen
import com.example.chamsocthucung2.view.user.Profile.NotificationSettingsScreen
import com.example.chamsocthucung2.view.user.Profile.ProfileScreen
import com.example.chamsocthucung2.view.user.Profile.UserTypeSelectionScreen
import com.example.chamsocthucung2.view.user.TaoHoSoScreen
import com.example.chamsocthucung2.view.user.user.BookingDoctorScreen
import com.example.chamsocthucung2.view.user.user.ChatScreen
import com.example.chamsocthucung2.viewmodel.AppViewModel
import com.example.chamsocthucung2.viewmodel.login.LoginViewModel
import com.example.chamsocthucung2.viewmodel.user.PetViewModel
import com.example.chamsocthucung2.viewmodel.user.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object Routes {
    const val MAIN = "main_screen"  // Màn hình chính
    const val REGISTER = "Register_Screen"
    const val FORGOT_PASSWORD = "Forgot_Password_Screen"
    const val CONFIRM_NEW_PASSWORD = "confirm_new_password_screen"
    const val VERIFY_PASSWORD = "verify_password_screen"
    const val HOME = "home_screen"
    const val PET_INFO = "pet_info"
    const val PET_PROFILE_DETAIL = "pet_profiledetail"  // Màn hình chi tiết thông tin thú cưng
    const val TAO_HO_SO = "tao_ho_so"
    const val BOOKING = "booking_screen"
    const val APPOINTMENT_DETAIL = "appointment_detail_screen"
    const val PROFILE = "profile_screen"  // Màn hình profile
    const val PROFILE_DOCTOR = "profile_doctor_screen" // ProfileDoctorScreen
    const val PASSWORD = "password_screen"
    const val SETTINGS = "settings_screen"
    const val NOTIFICATIONS = "notifications_screen"
    const val ABOUT = "about_screen"
    const val CHAT = "chat_screen" // Sử dụng hằng số Routes.CHAT
    const val ADVICE = "advice_screen"
    const val CARE_GUIDE = "care_guide_screen"
    const val CHANGE_PASSWORD = "change_password_screen"
    const val NOTIFICATION_SETTINGS = "notification_settings_screen"
    const val CUSTOMIZATION = "customization_screen"
    const val ABOUT_APP = "about_app_screen"
    const val ACCOUNT_INFO = "account_info_screen"
    const val SESSION_ACTIVITY_SCREEN = "session_activity_screen"
    const val USER_TYPE_SELECTION = "user_type_selection_screen"
    const val DOCTOR_TERMS = "doctor_terms_screen"
    const val DOCTOR_LOGIN = "doctor_login_screen"
    const val DOCTOR_DASHBOARD = "doctor_dashboard_screen"
    const val HOMEDOCTOR = "homedoctor_screen"
    const val DOCTOR_INFO = "doctor_info_screen"
    const val BOOKING_DOCTOR = "booking_doctor_screen" // BookingDoctorScreen
    const val RECEIVE_SCHEDULE = "receive_schedule_screen" // ReceiveScheduleScreen
}

@Composable
fun NavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel, // Thêm AppViewModel
    mAuth: FirebaseAuth,
    loginViewModel: LoginViewModel,
    googleSignIn: () -> Unit,
    onSignSuccess:() -> Unit,
    signOut: () -> Unit,
    changeTheme: (Color) -> Unit,
    startDestination: String
) {
    val petViewModel: PetViewModel = viewModel()
    val isLoggedIn by appViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.HOME else Routes.MAIN
    ) {
        // 🔹 Màn hình xác thực
        composable(Routes.MAIN) { MainScreen(navController, viewModel()) }
        composable(Routes.REGISTER) {
            RegisterScreen(navController, loginViewModel) { userInfo ->
                appViewModel.setLoggedIn(true)
                appViewModel.updateUserInfo(userInfo)
                navController.navigate(Routes.TAO_HO_SO)
            }
        }
        composable(Routes.FORGOT_PASSWORD) { ForgotPasswordScreen(navController) }
        composable(Routes.CONFIRM_NEW_PASSWORD) { ConfirmNewPasswordScreen(navController) }

        // 🔹 Màn hình chính
        composable(Routes.HOME) { HomeScreen(navController) }

        // 🔹 Màn hình thông tin thú cưng
        composable(Routes.TAO_HO_SO) {
            TaoHoSoScreen(
                navController,
                appViewModel
            )
        }

        // Truyền appViewModel
        composable(Routes.PET_INFO) { PetInfoScreen(navController) }
        composable(Routes.PET_PROFILE_DETAIL) { PetProfileDetailScreen(navController) }

        composable(Routes.APPOINTMENT_DETAIL) {
            AppointmentDetailScreen(navController, petViewModel)
        }

        // 🔹 Màn hình đặt lịch và chi tiết cuộc hẹn, đặt lịch khám
        composable(Routes.BOOKING) { BookingScreen(navController, petViewModel) }
        composable(Routes.BOOKING_DOCTOR) { BookingDoctorScreen(navController) }


        //Màn hình profile
        composable(Routes.PROFILE) {
            val context = LocalContext.current
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val repository = ProfileRepositoryImpl()

            val profileViewModel: ProfileViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProfileViewModel(repository) as T
                }
            })

            ProfileScreen(
                viewModel = profileViewModel,
                navController = navController
            )

        }
        composable(Routes.PROFILE_DOCTOR) {
            val context = LocalContext.current
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val repository = ProfileRepositoryImpl()

            val profileViewModel: ProfileViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProfileViewModel(repository) as T
                }
            })
            ProfileDoctorScreen(
                viewModel = profileViewModel,
                navController = navController,
                onLogout = { navController.navigate(Routes.MAIN) }
            )
        }
        composable(Routes.CHAT)
        { ChatScreen(rememberNavController()) }

        composable(Routes.ADVICE) {
            AdviceScreen(navController)
        }
        composable("care_guide_screen"){ CareGuideScreen (navController) }

        //Màng hình setting
        composable(Routes.NOTIFICATION_SETTINGS) {
            NotificationSettingsScreen(navController = navController)
        }
        composable(Routes.CUSTOMIZATION) {
            CustomizationScreen(
                navController = navController,
                loginViewModel = loginViewModel,
                signOut = {   navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.HOME) { inclusive = true }
                }
                }
            )
        }
        composable(Routes.ABOUT_APP) {
            AboutAppScreen(navController = navController)
        }

        composable(Routes.USER_TYPE_SELECTION) {
            UserTypeSelectionScreen(navController)
        }
        composable(Routes.DOCTOR_TERMS) {
            DoctorTermsScreen(navController = navController,
                loginViewModel = loginViewModel,
                signOut = {   navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.HOME) { inclusive = true }
                }
                }
            )
        }

        //điều hường màng hình bác sĩ
        composable(Routes.DOCTOR_LOGIN) {
            DoctorLoginScreen(navController, viewModel())
        }
        composable(Routes.HOMEDOCTOR) {
            HomeDoctorScreen(navController)
        }
        composable(Routes.DOCTOR_INFO) {
            DoctorInfoScreen(navController)
        }
        composable(Routes.RECEIVE_SCHEDULE) {
            ReceiveScheduleScreen(viewModel())
        }
    }

}