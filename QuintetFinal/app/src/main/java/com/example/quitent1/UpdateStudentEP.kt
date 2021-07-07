package com.example.quitent1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import kotlinx.android.synthetic.main.activity_update_student_ep.*

class UpdateStudentEP : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student_ep)

        update_stud_pass_submit.setOnClickListener {
            onclick()
        }

        update_stud_email_submit.setOnClickListener {
            onclick1()
        }

    }

    fun displayMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
    }

    private fun onclick() {
        val password = update_stud_password.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(password)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                val cUser = FireStoreClass().getCurrentUserRef()
                cUser!!.updatePassword(password).addOnSuccessListener {
                    StoreData.cStud!!.studPassword = password
                    FireStoreClass().updateStudEP(this, StoreData.cStud!!, 1)
                    Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun onclick1() {
        val email = update_stud_email.text.toString().trim { it <= ' ' }

        try {
            if (validateForm1(email)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                val cUser = FireStoreClass().getCurrentUserRef()
                cUser!!.updateEmail(email).addOnSuccessListener {
                    StoreData.cStud!!.studEmail = email
                    FireStoreClass().updateStudEP(this, StoreData.cStud!!, 2)
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


    private fun validateForm(
        password: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please Enter Password")
                false
            }
            else -> true
        }
    }

    private fun validateForm1(
        email: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please Enter Email")
                false
            }
            else -> true
        }
    }

}