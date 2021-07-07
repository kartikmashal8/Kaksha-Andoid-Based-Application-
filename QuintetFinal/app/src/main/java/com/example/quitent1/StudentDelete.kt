package com.example.quitent1

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.student
import com.example.quitent1.utils.Constants
import kotlinx.android.synthetic.main.activity_student_delete.*

class StudentDelete : CommonData() {
    private var adapter: LoadStudentsNames? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_delete)

        val spinner1 = findViewById<View>(R.id.search_branch_stud_spinner) as Spinner
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
                search_branch_stud.text = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val spinner2 = findViewById<View>(R.id.search_sem_std_spinner) as Spinner

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
                search_sem_std.text = categories2[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        val spinner3 = findViewById<View>(R.id.search_div_stud_spinner) as Spinner

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
                search_div_stud.text = categories3[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        search_students.setOnClickListener {
            onClick()
        }

        try {
            show_students.setOnItemClickListener { adapterView, view, i, l ->
                val temp = adapterView.adapter.getItem(i) as student
                val inflater: LayoutInflater =
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view1 = inflater.inflate(R.layout.display_warning_message, null)
                val popupWindow = PopupWindow(
                    view1,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                popupWindow.elevation = 10.0F
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                val bYes = view1.findViewById<Button>(R.id.staff_delete_yes)
                val bNo = view1.findViewById<Button>(R.id.staff_delete_no)

                bYes.setOnClickListener {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FireStoreClass().deleteStudent(this, temp)
                    adapter!!.remove(temp)
                    popupWindow.dismiss()
                }

                bNo.setOnClickListener {
                    popupWindow.dismiss()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
    }

    private fun onClick() {
        val branch = search_branch_stud.text.toString().trim { it <= ' ' }
        val sem = search_sem_std.text.toString().trim { it <= ' ' }
        val div = search_div_stud.text.toString().trim { it <= ' ' }

        if (validateForm(branch, sem, div)) {
            val studList: ArrayList<student> = ArrayList()
            mFireStore.collection(Constants.student).get().addOnSuccessListener {
                if (it.documents.size != 0) {
                    for (i in it.documents) {
                        val temp = i.toObject(student::class.java)!!
                        if (temp.studBranch.equals(branch, ignoreCase = true)
                            && temp.studSem == sem.toInt()
                            && temp.studDiv.equals(div, ignoreCase = true)
                        ) {
                            studList.add(temp)
                        }
                    }
                }
                if (studList.size != 0) {
                    show_students.visibility = View.VISIBLE
                    student_delete_message.visibility = View.GONE
                    adapter = LoadStudentsNames(this, R.layout.stud_display_shape, studList)
                    show_students.adapter = adapter
                } else {
                    show_students.visibility = View.GONE
                    student_delete_message.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validateForm(
        branch: String,
        sem: String,
        div: String
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
            if (check == 0) {
                showErrorSnackBar("Please Enter Sem")
                return false
            }
        } else {
            showErrorSnackBar("Please Enter Sem")
            return false
        }
        return when {
            TextUtils.isEmpty(branch) -> {
                showErrorSnackBar("Please Enter Name")
                false
            }
            TextUtils.isEmpty(div) -> {
                showErrorSnackBar("Please Enter Division")
                false
            }
            else -> true
        }
    }

    class LoadStudentsNames(var mCtx: Context, var resources: Int, var items: List<student>) :
        ArrayAdapter<student>(mCtx, resources, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutIn: LayoutInflater = LayoutInflater.from(mCtx)
            val vw: View = layoutIn.inflate(resources, null)

            val sName: TextView = vw.findViewById(R.id.stud_shape_name)
            val sUSN: TextView = vw.findViewById(R.id.stud_shape_usn)
            val oClass: student = items[position]

            sName.text = oClass.studName
            sUSN.text = oClass.studUSN

            return vw
        }
    }

}