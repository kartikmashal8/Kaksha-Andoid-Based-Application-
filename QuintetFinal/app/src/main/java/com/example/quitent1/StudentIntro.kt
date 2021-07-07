package com.example.quitent1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.student
import kotlinx.android.synthetic.main.activity_student_intro.*

class StudentIntro : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_intro)

        student_intro_sign_up.setOnClickListener {
            val intent = Intent(this, StudentSignUp::class.java)
            startActivity(intent)
        }

        student_intro_sign_in.setOnClickListener {
            if (FireStoreClass().getCurrentUserId().isNotEmpty()) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FireStoreClass().autoLoadData(this)
            } else {
                val intent = Intent(this, StudentSignIn::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun signInSuccess(userInfo: student?) {
        StoreData.cStud = userInfo
        hideProgressDialog()
        startActivity(Intent(this, StudentActivity::class.java))
        finish()
    }

    fun signInFailed() {
        Toast.makeText(this, "Sign-In Failed", Toast.LENGTH_LONG).show()
        hideProgressDialog()
    }
}