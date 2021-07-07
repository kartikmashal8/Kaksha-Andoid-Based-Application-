package com.example.quitent1

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_staff_quiz_marks.*

class StaffQuizMarks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_quiz_marks)

        val arr: ArrayList<String> = ArrayList()
        for (key in StoreData.cQuiz!!.qMarks.keys) {
            arr.add(key + " : " + StoreData.cQuiz!!.qMarks[key])
        }
        if (arr.size != 0) {
            staff_quiz_student_marks.visibility = View.VISIBLE
            staff_quiz_message.visibility = View.GONE
            val adapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
            staff_quiz_student_marks.adapter = adapter
        } else {
            staff_quiz_student_marks.visibility = View.GONE
            staff_quiz_message.visibility = View.VISIBLE
        }
    }
}