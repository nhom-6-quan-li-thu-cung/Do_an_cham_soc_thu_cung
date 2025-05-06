package com.example.chamsocthucung2.data.model.doctor

data class AdviceItem(
    val id: String = "",
    val question: String = "",
    val answer: String = "",
    val isExpanded: Boolean = false // Thêm trường để kiểm soát trạng thái mở rộng câu trả lời

)
