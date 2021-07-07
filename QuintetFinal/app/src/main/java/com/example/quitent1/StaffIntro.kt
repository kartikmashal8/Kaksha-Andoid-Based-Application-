package com.example.quitent1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.Staff
import kotlinx.android.synthetic.main.activity_staff_intro.*

class StaffIntro : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_intro)

        staff_intro_sign_up.setOnClickListener {
            val intent = Intent(this, StaffSignUp::class.java)
            startActivity(intent)
        }

        staff_intro_sign_in.setOnClickListener {
            if (FireStoreClass().getCurrentUserId().isNotEmpty()) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FireStoreClass().autoLoadData(this)
            } else {
                val intent = Intent(this, StaffSignIn::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun signInSuccess(userInfo: Staff?) {
        StoreData.cStaff = userInfo
        hideProgressDialog()
        startActivity(Intent(this, StaffActivity::class.java))
        finish()
    }

    fun signInFailed() {
        Toast.makeText(this, "Sign-In Failed", Toast.LENGTH_LONG).show()
        hideProgressDialog()
    }
}