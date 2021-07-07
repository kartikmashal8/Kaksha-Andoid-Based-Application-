package com.example.quitent1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.Staff
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_staff_sign_in.*
import java.io.IOException

class StaffSignIn : CommonData() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_sign_in)

        staff_sign_in_submit.setOnClickListener {
            onClick()
        }
    }

    fun signInSuccess(userInfo: Staff?) {
        StoreData.cStaff = userInfo

        try {
            val fileName = "logininfo.txt"
            var fileBody = ""
            val context: Context = this
            context.openFileInput(fileName).use { stream ->
                fileBody = stream.bufferedReader().use {
                    it.readText()
                }

            }
            if (fileBody == "890765") {

            } else {
                val fileName = "logininfo.txt"
                val id = userInfo!!.staffId
                val fileBody = "457355-$id"
                val context: Context = this
                context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                    output.write(fileBody.toByteArray())
                }
            }
        } catch (ffe: IOException) {
            print(ffe.message)
        }




        hideProgressDialog()
        startActivity(Intent(this, StaffActivity::class.java))
        finish()
    }

    private fun onClick() {
        val email = staff_sign_in_email.text.toString().trim { it <= ' ' }
        val password = staff_sign_in_password.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(email, password)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // If sign in successful
                            FireStoreClass().loadUserData(this)
                        } else {
                            // If sign in fails
                            Log.w("Sign in", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                        }
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            hideProgressDialog()
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please Enter Email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please Enter Password")
                false
            }
            else -> true
        }
    }

}