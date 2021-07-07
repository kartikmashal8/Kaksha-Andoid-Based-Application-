package com.example.quitent1.model

import java.util.*

data class ClassModel(
    val classId: String = "",
    val className: String = "",
    val staffId: String = "",
    val classSem: Int = 0,
    val classDiv: String = "",
    val classSubCode: String = "",
    val classBranch: String = "",
    val classStudents: ArrayList<String> = ArrayList(),
    val classPDF: ArrayList<String> = ArrayList(),
    val classNotice: ArrayList<String> = ArrayList(),
    val classQuiz: MutableMap<String, String> = mutableMapOf()
)
