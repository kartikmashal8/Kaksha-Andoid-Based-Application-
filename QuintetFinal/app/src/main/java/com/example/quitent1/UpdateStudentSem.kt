package com.example.quitent1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import kotlinx.android.synthetic.main.activity_update_student_sem.*

class UpdateStudentSem : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student_sem)

        val spinner2 = findViewById<View>(R.id.update_stud_sem_spinner) as Spinner
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
                update_stud_sem.text = categories2[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        update_stud_sem_submit.setOnClickListener {
            onclick()
        }
    }

    fun displayMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
        finish()
    }

    private fun onclick() {
        val sem = update_stud_sem.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(sem)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                StoreData.cStud!!.studSem = sem.toInt()
                FireStoreClass().updateSem(this, StoreData.cStud!!)
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun validateForm(
        sem: String
    ): Boolean {
        if (sem.isNotEmpty()) {
            var check = 0
            val temp = sem.toInt()
            for (i in 1 until 9) {
                if (i == temp) {
                    check = 1
                    break
                }
            }
            return if (check == 0) {
                showErrorSnackBar("Please Enter Sem")
                false
            } else {
                true
            }
        } else {
            showErrorSnackBar("Please Enter Sem")
            return false
        }
    }
}