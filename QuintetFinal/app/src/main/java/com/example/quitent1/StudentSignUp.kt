package com.example.quitent1

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_student_sign_up.*

class StudentSignUp : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign_up)

        val spinner1 = findViewById<View>(R.id.student_sign_up_branch_spinner) as Spinner

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
                student_sign_up_branch.text = categories[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val spinner2 = findViewById<View>(R.id.student_sign_up_sem_spinner) as Spinner

        val categories2: MutableList<String> = ArrayList()
        categories2.add("1")
        categories2.add("2")
        categories2.add("3")
        categories2.add("4")
        categories2.add("5")
        categories2.add("6")
        categories2.add("7")
        categories2.add("8")

        val dataAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories2)

        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner2.adapter = dataAdapter2

        spinner2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                student_sign_up_sem.text = categories2[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        val spinner3 = findViewById<View>(R.id.student_sign_up_div_spinner) as Spinner

        val categories3: MutableList<String> = ArrayList()
        categories3.add("A")
        categories3.add("B")
        categories3.add("C")
        categories3.add("D")
        categories3.add("E")
        categories3.add("F")
        categories3.add("G")
        categories3.add("H")

        val dataAdapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories3)

        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner3.adapter = dataAdapter3

        spinner3.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                student_sign_up_div.text = categories3[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        student_sign_up_submit.setOnClickListener {
            onclick()
        }

    }

    fun studRegisteredSuccess() {
        Toast.makeText(
            this,
            "Sign up successful",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    fun studRegisteredFailure() {
        Toast.makeText(
            this,
            "Sign up Un-successful",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        finish()
    }

    private fun onclick() {
        val studName = student_sign_up_name.text.toString().trim { it <= ' ' }
        val studBranch = student_sign_up_branch.text.toString().trim { it <= ' ' }
        val studSem = student_sign_up_sem.text.toString().trim { it <= ' ' }
        val studUSN = student_sign_up_USN.text.toString().trim { it <= ' ' }
        val studDiv = student_sign_up_div.text.toString().trim { it <= ' ' }
        val studEmail = student_sign_up_email.text.toString().trim { it <= ' ' }
        val studPassword = student_sign_up_password.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(
                    studName,
                    studBranch,
                    studSem,
                    studUSN,
                    studDiv,
                    studEmail,
                    studPassword
                )
            ) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(studEmail, studPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val registeredEmail = firebaseUser.email!!
                            val user =
                                student(
                                    firebaseUser.uid,
                                    studName,
                                    studBranch,
                                    studSem.toInt(),
                                    studUSN,
                                    studDiv,
                                    registeredEmail,
                                    studPassword
                                )
                            FireStoreClass().registerStudent(this, user)
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
        studName: String,
        studBranch: String,
        studSem: String,
        studUSN: String,
        studDiv: String,
        email: String,
        password: String
    ): Boolean {
        if (studSem.isNotEmpty()) {
            var check = 0
            val temp = studSem.toInt()
            for (i in 1 until 9) {
                if (i == temp) {
                    check = 1
                    break
                }
            }
            if (check == 0) {
                showErrorSnackBar("Please Enter Valid Sem")
                return false
            }
        } else {
            showErrorSnackBar("Please Enter Sem")
            return false
        }
        return when {
            TextUtils.isEmpty(studName) -> {
                showErrorSnackBar("Please Enter Name")
                false
            }
            TextUtils.isEmpty(studBranch) -> {
                showErrorSnackBar("Please Enter Branch")
                false
            }
            TextUtils.isEmpty(studUSN) -> {
                showErrorSnackBar("Please Enter USN")
                false
            }
            TextUtils.isEmpty(studDiv) -> {
                showErrorSnackBar("Please Enter Division")
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