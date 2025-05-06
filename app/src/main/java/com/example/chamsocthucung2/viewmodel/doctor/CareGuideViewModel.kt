package com.example.chamsocthucung2.viewmodel.doctor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chamsocthucung2.data.model.doctor.CareGuide
import com.google.firebase.database.FirebaseDatabase

class CareGuideViewModel : ViewModel() {
    private val _careGuides = MutableLiveData<Map<String, CareGuide>>()
    val careGuides: LiveData<Map<String, CareGuide>> get() = _careGuides

    private val database = FirebaseDatabase.getInstance().reference

    init {
        loadCareGuides()
    }

    private fun loadCareGuides() {
        database.child("care_guides")
            .get()
            .addOnSuccessListener { snapshot ->
                val guidesMap = snapshot.children.associate {
                    it.key!! to it.getValue(CareGuide::class.java)!!
                }
                _careGuides.value = guidesMap
                Log.d("CareGuideViewModel", "Dữ liệu chăm sóc đã tải về: $guidesMap")
            }
            .addOnFailureListener { exception ->
                Log.e("CareGuideViewModel", "Lỗi khi tải dữ liệu chăm sóc: ", exception)
            }
    }
}

