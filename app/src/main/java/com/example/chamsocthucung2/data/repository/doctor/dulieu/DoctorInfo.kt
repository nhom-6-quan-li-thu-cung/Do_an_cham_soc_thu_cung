package com.example.chamsocthucung2.data.repository.doctor.dulieu

data class DoctorInfo(
    val id: String = "",
    val name: String = "",
    val age: Int = 0,
    val email: String = "",
    val phone: String = "",
    val hospital: String = "",
    val specialty: String = "", // vẫn giữ lại nếu bạn đã dùng chỗ khác
    val mainSpecialty: String = "", // <-- chuyên môn chính (thêm mới)
    val specialties: List<String> = emptyList(), // đa chuyên môn
    val experienceYears: Int = 0
) {
}
