package com.example.chamsocthucung2.view.doctor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime. livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chamsocthucung2.viewmodel.doctor.CareGuideViewModel

@Composable
fun CareGuideScreen(navController: NavController, viewModel: CareGuideViewModel = viewModel()) {
    val guides by viewModel.careGuides.observeAsState(emptyMap()) // Đảm bảo guides không phải là null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Button
        IconButton(
            onClick = { navController.popBackStack() }, // Quay lại màn hình trước đó
            modifier = Modifier
                .align(Alignment.Start) // Đặt nút ở góc trái
                .padding(top = 20.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Quay lại")
        }

        // Tiêu đề "Chăm sóc" canh giữa và có thanh kẻ ngang dưới
        Text(
            text = "Chăm sóc 🐾",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Center
        )

        // Thanh kẻ ngang dưới tiêu đề
        Divider(
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )

        // Danh sách các hướng dẫn chăm sóc
        LazyColumn {
            guides.forEach { (key, guide) -> // Sử dụng forEach để duyệt qua các item
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = guide.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            guide.steps.forEachIndexed { i, step ->
                                Text(text = "${i + 1}. $step")
                            }
                        }
                    }
                }
            }
        }
    }
}
