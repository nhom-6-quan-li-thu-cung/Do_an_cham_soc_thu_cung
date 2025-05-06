package com.example.chamsocthucung2.viewmodel.doctor

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.chamsocthucung2.data.model.doctor.AdviceItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdviceViewModel : ViewModel() {
    private val _adviceList = mutableStateListOf<AdviceItem>()
    val adviceList: List<AdviceItem> = _adviceList

    private val firestore = FirebaseFirestore.getInstance()

    init {
        loadAdvice()
    }

    private fun loadAdvice() {
        firestore.collection("Tuvan")
            .get()
            .addOnSuccessListener { snap ->
                // Tạo danh sách các câu hỏi và câu trả lời từ Firestore
                val list = snap.documents.map { doc ->
                    AdviceItem(
                        id = doc.id,
                        question = doc.getString("Question") ?: "",
                        answer = doc.getString("Answer") ?: "" // Hoặc "Answan" nếu cần
                    )
                }
                // Cập nhật danh sách vào _adviceList để thông báo Compose
                _adviceList.clear()  // Xóa danh sách cũ
                _adviceList.addAll(list)  // Thêm dữ liệu mới vào
            }
            .addOnFailureListener { exception ->
                // Xử lý khi có lỗi
                Log.e("AdviceRepository", "Error getting documents: ", exception)
            }
    }

    fun toggleExpand(id: String) {
        val index = _adviceList.indexOfFirst { it.id == id }
        if (index != -1) {
            _adviceList[index] = _adviceList[index].copy(isExpanded = !_adviceList[index].isExpanded)
        }
    }
}
