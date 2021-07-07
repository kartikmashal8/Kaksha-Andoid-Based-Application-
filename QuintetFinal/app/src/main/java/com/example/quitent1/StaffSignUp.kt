package com.example.quitent1

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.Staff
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_staff_sign_up.*

class StaffSignUp : CommonData() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_sign_up)

        val spinner1 = findViewById<View>(R.id.staff_sign_up_branch_spinner) as Spinner
        val categories: MutableList<String> = ArrayList()
        categories.add("CS")
        categories.add("EC")
        categories.add("ME")
        categories.add("IS")
        categories.add("CV")
        categories.add("EE")

        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1.adapter = dataAdapter

        spinner1.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                staff_sign_up_branch.text = categories[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        staff_sign_up_submit.setOnClickListener {
            onclick()
        }

    }

    fun staffRegisteredSuccess() {
        Toast.makeText(
            this,
            "Sign up successful",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    fun staffRegisteredFailure() {
        Toast.makeText(
            this,
            "Sign up Un-successful",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        finish()
    }

    private fun onclick() {
        val name = staff_sign_up_name.text.toString().trim { it <= ' ' }
        val branch = staff_sign_up_branch.text.toString().trim { it <= ' ' }
        val email = staff_sign_up_email.text.toString().trim { it <= ' ' }
        val password = staff_sign_up_password.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(name, branch, email, password)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val registeredEmail = firebaseUser.email!!
                            val user =
                                Staff(firebaseUser.uid, name, branch, registeredEmail, password)
                            FireStoreClass().registerStaff(this, user)
                        } else {
                            Toast.makeText(
                                this,
                                task.exception!!.message, Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                            finish()
                        }
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun validateForm(
        name: String,
        branch: String,
        email: String,
        password: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please Enter Name")
                false
            }
            TextUtils.isEmpty(branch) -> {
                showErrorSnackBar("Please Enter Branch")
                false
            }
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

