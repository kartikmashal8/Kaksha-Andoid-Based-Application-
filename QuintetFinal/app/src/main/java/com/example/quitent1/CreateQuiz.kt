package com.example.quitent1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.QuizModel
import com.example.quitent1.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_create_quiz.*

class CreateQuiz : CommonData() {

    private var cQuizModel: QuizModel = QuizModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        quiz_add_question.setOnClickListener {
            onAddQuestionClick()
        }

        quiz_submit.setOnClickListener {
            onQuizSubmitClick()
        }
    }


    private fun onAddQuestionClick() {
        val qQuestion = quiz_question.text.toString().trim { it <= ' ' }
        val qOption1 = quiz_option1.text.toString().trim { it <= ' ' }
        val qOption2 = quiz_option2.text.toString().trim { it <= ' ' }
        val qOption3 = quiz_option3.text.toString().trim { it <= ' ' }
        val qOption4 = quiz_option4.text.toString().trim { it <= ' ' }
        val qCorrectOption = quiz_correct_option.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(qQuestion, qOption1, qOption2, qOption3, qOption4, qCorrectOption)) {
                cQuizModel.arrQuestions.add(qQuestion)
                cQuizModel.arrOptions.add(qOption1)
                cQuizModel.arrOptions.add(qOption2)
                cQuizModel.arrOptions.add(qOption3)
                cQuizModel.arrOptions.add(qOption4)
                cQuizModel.arrCorrectOptions.add(qCorrectOption.toInt())

                quiz_question.setText("")
                quiz_option1.setText("")
                quiz_option2.setText("")
                quiz_option3.setText("")
                quiz_option4.setText("")
                quiz_correct_option.setText("")

                Toast.makeText(this, "Question added successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun displayMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
        finish()
    }

    private fun onQuizSubmitClick() {
        val qName = quiz_name.text.toString()

        if (qName.isNotEmpty()) {
            cQuizModel.qName = qName
            try {
                showProgressDialog(resources.getString(R.string.please_wait))
                val temp = FirebaseFirestore.getInstance().collection(Constants.quizzes).document()
                cQuizModel.qId = temp.id
                temp.set(cQuizModel, SetOptions.merge()).addOnSuccessListener {
                    StoreData.cClass!!.classQuiz[temp.id] = qName
                    FireStoreClass().writeBoardContent(this, StoreData.cClass!!)
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                hideProgressDialog()
            }
        } else {
            Toast.makeText(this, "Quiz Name Is Empty", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateForm(
        qQuestion: String,
        qOption1: String,
        qOption2: String,
        qOption3: String,
        qOption4: String,
        qCorrectOption: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(qQuestion) -> {
                showErrorSnackBar("Please Enter Question")
                false
            }
            TextUtils.isEmpty(qOption1) -> {
                showErrorSnackBar("Please Enter Option1")
                false
            }
            TextUtils.isEmpty(qOption2) -> {
                showErrorSnackBar("Please Enter Option2")
                false
            }
            TextUtils.isEmpty(qOption3) -> {
                showErrorSnackBar("Please Enter Option3")
                false
            }
            TextUtils.isEmpty(qOption4) -> {
                showErrorSnackBar("Please Enter Option4")
                false
            }
            TextUtils.isEmpty(qCorrectOption) -> {
                showErrorSnackBar("Please Enter CorrectOption")
                false
            }
            else -> true
        }
    }
}