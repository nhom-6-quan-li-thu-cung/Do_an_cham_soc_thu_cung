package com.example.chamsocthucung2.view.doctor

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chamsocthucung2.data.model.doctor.AdviceItem
import com.example.chamsocthucung2.viewmodel.doctor.AdviceViewModel

@Composable
fun AdviceScreen(navController: NavController, viewModel: AdviceViewModel = viewModel()) {
    val adviceList = viewModel.adviceList

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

        // Title
        Text(
            text = "Câu hỏi thường gặp 🐾",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 20.dp) // Đẩy xuống một chút
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Horizontal Divider
        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        // List of items (advice)
        LazyColumn {
            items(adviceList) { item ->
                AdviceCard(
                    item = item,
                    onToggle = { viewModel.toggleExpand(item.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun AdviceCard(
    item: AdviceItem,
    onToggle: () -> Unit
) {
    Log.d("Tuvan", "Question: ${item.question}, isExpanded: ${item.isExpanded}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.question,
                style = MaterialTheme.typography.titleMedium
            )

            if (item.isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
