package com.example.quitent1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import kotlinx.android.synthetic.main.activity_update_admin_ep.*

class UpdateAdminEP : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_admin_ep)

        update_admin_pass_submit.setOnClickListener {
            onclick()
        }

        update_admin_email_submit.setOnClickListener {
            onclick1()
        }
    }

    fun displayMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
        update_admin_email.text!!.clear()
        update_admin_password.text!!.clear()
    }

    private fun onclick() {
        val password = update_admin_password.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(password)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                val cUser = FireStoreClass().getCurrentUserRef()
                cUser!!.updatePassword(password).addOnSuccessListener {
                    StoreData.cAdmin!!.adminPassword = password
                    FireStoreClass().updateAdminEP(this, StoreData.cAdmin!!, 1)
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun onclick1() {
        val email = update_admin_email.text.toString().trim { it <= ' ' }

        try {
            if (validateForm1(email)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                val cUser = FireStoreClass().getCurrentUserRef()
                cUser!!.updateEmail(email).addOnSuccessListener {
                    StoreData.cAdmin!!.adminEmail = email
                    FireStoreClass().updateAdminEP(this, StoreData.cAdmin!!, 2)
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