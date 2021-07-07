package com.example.quitent1.model

data class QuizModel(
    var qId: String = "",
    var qName: String = "",
    val arrQuestions: ArrayList<String> = ArrayList(),
    val arrOptions: ArrayList<String> = ArrayList(),
    val arrCorrectOptions: ArrayList<Int> = ArrayList(),
    val qMarks: MutableMap<String, String> = mutableMapOf()
)
