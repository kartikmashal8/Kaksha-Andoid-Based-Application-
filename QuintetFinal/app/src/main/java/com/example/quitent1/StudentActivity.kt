package com.example.quitent1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.quitent1.model.ClassModel
import com.example.quitent1.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_student.*
import kotlinx.android.synthetic.main.student_content.*

class StudentActivity : CommonData(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        nav_view1.setNavigationItemSelectedListener(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        student_class.setOnItemClickListener { adapterView, view, i, l ->
            val temp = adapterView.adapter.getItem(i) as ClassModel
            StoreData.cClass = temp
            val intent = Intent(this, BoardContent1::class.java)
            startActivity(intent)
        }

    }

    private fun download() {
        try {
            val cStudBranch = StoreData.cStud!!.studBranch
            val cStudSem = StoreData.cStud!!.studSem
            val cStudDiv = StoreData.cStud!!.studDiv
            mFireStore.collection(Constants.classes).whereEqualTo("classBranch", cStudBranch).get()
                .addOnSuccessListener { document ->
                    if (document.documents.isNotEmpty()) {
                        val adminsList: ArrayList<ClassModel> = ArrayList()
                        for (i in document.documents) {
                            val board = i.toObject(ClassModel::class.java)!!
                            if (board.classSem == cStudSem && board.classDiv == cStudDiv) {
                                adminsList.add(board)
                            }
                        }
                        if (adminsList.size > 0) {
                            student_message.visibility = View.GONE
                            student_class.visibility = View.VISIBLE
                            val adapter = LoadClass(this, R.layout.button_shape, adminsList)
                            student_class.adapter = adapter
                        } else {
                            student_message.visibility = View.VISIBLE
                            student_class.visibility = View.GONE
                        }
                    } else {
                        student_message.visibility = View.VISIBLE
                        student_class.visibility = View.GONE
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "No Class found", Toast.LENGTH_LONG).show()
                }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_refresh1 -> {
                download()
                toggleDrawer()
            }
            R.id.nav_update1 -> {
                startActivity(Intent(this, UpdateStudentEP::class.java))
            }
            R.id.nav_update_sem -> {
                startActivity(Intent(this, UpdateStudentSem::class.java))
            }
            R.id.nav_sign_out1 -> {
                FirebaseAuth.getInstance().signOut()
                finish()
            }
            R.id.nav_about_us1 -> {
                startActivity(Intent(this, About_us::class.java))
            }
        }
        return true
    }

    private fun toggleDrawer() {
        if (drawer_layout1.isDrawerOpen(GravityCompat.START)) {
            drawer_layout1.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout1.openDrawer(GravityCompat.START)
        }
    }


    class LoadClass(var mCtx: Context, var resources: Int, var items: List<ClassModel>) :
        ArrayAdapter<ClassModel>(mCtx, resources, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutIn: LayoutInflater = LayoutInflater.from(mCtx)
            val vw: View = layoutIn.inflate(resources, null)

            val bName: TextView = vw.findViewById(R.id.board_name)
            val bSubCd: TextView = vw.findViewById(R.id.board_subCode)
            val bDiv: TextView = vw.findViewById(R.id.board_div)
            val oClass: ClassModel = items[position]

            bName.text = oClass.className
            bSubCd.text = oClass.classSubCode
            bDiv.text = oClass.classDiv

            return vw
        }
    }

}