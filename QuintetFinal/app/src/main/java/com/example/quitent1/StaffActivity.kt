package com.example.quitent1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.ClassModel
import com.example.quitent1.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_staff.*
import kotlinx.android.synthetic.main.staff_content.*

class StaffActivity : CommonData(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)

        nav_view.setNavigationItemSelectedListener(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        staff_add_class.setOnClickListener {
            val intent = Intent(this, classModel::class.java)
            startActivity(intent)
        }

        staff_class.setOnItemClickListener { adapterView, view, i, l ->
            val temp = adapterView.adapter.getItem(i) as ClassModel
            StoreData.cClass = temp
            val intent = Intent(this, BoardContent::class.java)
            startActivity(intent)
        }
    }

    private fun download() {
        try {
            val cUser = FireStoreClass().getCurrentUserId()
            mFireStore.collection(Constants.classes).whereEqualTo("staffId", cUser).get()
                .addOnSuccessListener { document ->
                    if (document.documents.isNotEmpty()) {
                        staff_message.visibility = View.GONE
                        staff_class.visibility = View.VISIBLE
                        val adminsList: ArrayList<ClassModel> = ArrayList()
                        for (i in document.documents) {
                            val board = i.toObject(ClassModel::class.java)!!
                            adminsList.add(board)
                        }
                        val adapter = LoadClass(this, R.layout.button_shape, adminsList)
                        staff_class.adapter = adapter
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_refresh -> {
                download()
                toggleDrawer()
            }
            R.id.nav_change_password_E -> {
                startActivity(Intent(this, UpdateStaffEP::class.java))
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                finish()
            }
            R.id.nav_about_us -> {
                startActivity(Intent(this, About_us::class.java))
            }
        }
        return true
    }

    private fun toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
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
