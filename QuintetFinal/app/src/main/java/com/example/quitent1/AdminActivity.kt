package com.example.quitent1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.admin_content.*

class AdminActivity : CommonData(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        nav_view.setNavigationItemSelectedListener(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        add_admin.setOnClickListener {
            startActivity(Intent(this, AdminSignUp::class.java))
        }
        add_staff.setOnClickListener {
            startActivity(Intent(this, StaffSignUp::class.java))
        }

        add_student.setOnClickListener {
            startActivity(Intent(this, StudentSignUp::class.java))
        }

        delete_staff.setOnClickListener {
            startActivity(Intent(this, StaffDelete::class.java))
        }

        delete_student.setOnClickListener {
            startActivity(Intent(this, StudentDelete::class.java))
        }

        admin_to_staff_login.setOnClickListener {
            startActivity(Intent(this, StaffSignIn::class.java))
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_update2 -> {
                startActivity(Intent(this, UpdateAdminEP::class.java))
            }
            R.id.nav_sign_out2 -> {
                FirebaseAuth.getInstance().signOut()
                finish()
            }
            R.id.nav_about_us2 ->{
                startActivity(Intent(this, About_us::class.java))
            }
        }
        return true
    }

}