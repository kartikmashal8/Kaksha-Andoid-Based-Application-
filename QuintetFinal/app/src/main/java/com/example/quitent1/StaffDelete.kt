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
import com.example.quitent1.model.Staff
import com.example.quitent1.utils.Constants
import kotlinx.android.synthetic.main.activity_staff_delete.*

class StaffDelete : CommonData() {
    private var adapter: LoadStaffNames? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_delete)

        val spinner1 = findViewById<View>(R.id.search_branch_spinner) as Spinner
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
                search_branch.text = categories[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        search_staff.setOnClickListener {
            onClick()
        }

        try {
            show_staff.setOnItemClickListener { adapterView, view, i, l ->
                val temp = adapterView.adapter.getItem(i) as Staff
                val inflater: LayoutInflater =
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view1 = inflater.inflate(R.layout.display_warning_message, null)
                val popupWindow = PopupWindow(
                    view1,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                popupWindow.elevation = 10.0F
                popupWindow.showAtLocation(view, Gravity.TOP, 0, 0)

                val bYes = view1.findViewById<Button>(R.id.staff_delete_yes)
                val bNo = view1.findViewById<Button>(R.id.staff_delete_no)

                bYes.setOnClickListener {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FireStoreClass().deleteStaff(this, temp)
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
        val branch = search_branch.text.toString().trim { it <= ' ' }
        if (validateForm(branch)) {
            val staffList: ArrayList<Staff> = ArrayList()
            mFireStore.collection(Constants.staff).get().addOnSuccessListener {
                if (it.documents.size != 0) {
                    for (i in it.documents) {
                        val temp = i.toObject(Staff::class.java)!!
                        if (temp.staffBranch.equals(branch, ignoreCase = true)) {
                            staffList.add(temp)
                        }
                    }
                }
                if (staffList.size != 0) {
                    show_staff.visibility = View.VISIBLE
                    staff_delete_message.visibility = View.GONE
                    adapter = LoadStaffNames(this, R.layout.staff_display_shape, staffList)
                    show_staff.adapter = adapter
                } else {
                    show_staff.visibility = View.GONE
                    staff_delete_message.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validateForm(
        branch: String,
    ): Boolean {
        return when {
            TextUtils.isEmpty(branch) -> {
                showErrorSnackBar("Please Enter Branch")
                false
            }
            else -> true
        }
    }

    class LoadStaffNames(var mCtx: Context, var resources: Int, var items: List<Staff>) :
        ArrayAdapter<Staff>(mCtx, resources, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutIn: LayoutInflater = LayoutInflater.from(mCtx)
            val vw: View = layoutIn.inflate(resources, null)

            val sName: TextView = vw.findViewById(R.id.staff_delete_shape)
            val oClass: Staff = items[position]

            sName.text = oClass.staffName

            return vw
        }
    }
}