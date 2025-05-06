package com.example.chamsocthucung2.view.doctor

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.viewmodel.doctor.dulieu.DoctorInfoViewModel

@Composable
fun DoctorInfoScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: DoctorInfoViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DoctorInfoViewModel() as T
        }
    })

    val info = viewModel.doctorInfo.collectAsState().value
    val isFormValid = remember(info) {
        info.name.isNotBlank() &&
                info.age > 0 &&
                Patterns.EMAIL_ADDRESS.matcher(info.email).matches() &&
                info.phone.length == 10 &&
                info.hospital.isNotBlank() &&
                info.specialties.isNotEmpty() &&
                info.mainSpecialty.isNotBlank() &&
                info.experienceYears > 0
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Thông tin bác sĩ", fontSize = 20.sp)

            OutlinedTextField(
                value = info.name,
                onValueChange = { viewModel.updateInfo(info.copy(name = it)) },
                label = { Text("Họ tên") },
                singleLine = true
            )

            OutlinedTextField(
                value = if (info.age == 0) "" else info.age.toString(),
                onValueChange = {
                    viewModel.updateInfo(info.copy(age = it.toIntOrNull() ?: 0))
                },
                label = { Text("Tuổi") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = info.email,
                onValueChange = { viewModel.updateInfo(info.copy(email = it)) },
                label = { Text("Email") },
                isError = !Patterns.EMAIL_ADDRESS.matcher(info.email).matches(),
                singleLine = true
            )

            OutlinedTextField(
                value = info.phone,
                onValueChange = {
                    if (it.all { ch -> ch.isDigit() }) {
                        viewModel.updateInfo(info.copy(phone = it))
                    }
                },
                label = { Text("Số điện thoại") },
                isError = info.phone.length != 10,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = info.hospital,
                onValueChange = { viewModel.updateInfo(info.copy(hospital = it)) },
                label = { Text("Cơ sở khám chữa bệnh") },
                singleLine = true
            )

            MultiSelectDropdown(
                selectedItems = info.specialties,
                onSelectionChanged = { viewModel.updateInfo(info.copy(specialties = it)) }
            )

            MainSpecialtyDropdown(
                selected = info.mainSpecialty,
                onSelectedChange = { viewModel.updateInfo(info.copy(mainSpecialty = it)) }
            )

            OutlinedTextField(
                value = if (info.experienceYears == 0) "" else info.experienceYears.toString(),
                onValueChange = {
                    viewModel.updateInfo(info.copy(experienceYears = it.toIntOrNull() ?: 0))
                },
                label = { Text("Tuổi nghề") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Button(
                onClick = {
                    loading = true
                    viewModel.saveToFirestore(
                        onSuccess = {
                            loading = false
                            navController.navigate(Routes.HOMEDOCTOR)
                        },
                        onError = {
                            loading = false
                            errorMessage = it.toString()
                        }
                    )
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF9800))
            ) {
                Text("Tiếp tục", color = Color.White)
            }

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            errorMessage?.let {
                Text("Lỗi: $it", color = Color.Red)
            }
        }
    }
}

@Composable
fun MultiSelectDropdown(
    selectedItems: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    val options = listOf("Nội khoa", "Ngoại khoa", "Da liễu", "Tai mũi họng", "Nhi khoa")
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedItems.joinToString(", "),
            onValueChange = {},
            label = { Text("Đa các chuyên môn") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                val isSelected = selectedItems.contains(option)
                DropdownMenuItem(onClick = {
                    val newSelection = if (isSelected) {
                        selectedItems - option
                    } else {
                        selectedItems + option
                    }
                    onSelectionChanged(newSelection)
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isSelected, onCheckedChange = null)
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
fun MainSpecialtyDropdown(
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    val options = listOf("Nội khoa", "Ngoại khoa", "Da liễu", "Tai mũi họng", "Nhi khoa")
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text("Chuyên môn chính") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onSelectedChange(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}
