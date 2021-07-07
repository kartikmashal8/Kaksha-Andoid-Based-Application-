package com.example.quitent1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.Admin
import kotlinx.android.synthetic.main.activity_admin_intro.*

class AdminIntro : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_intro)

        admin_intro_sign_up.setOnClickListener {
            val intent = Intent(this, AdminSignUp::class.java)
            startActivity(intent)
        }

        try {
            admin_intro_sign_in.setOnClickListener {
                if (FireStoreClass().getCurrentUserId().isNotEmpty()) {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FireStoreClass().autoLoadData(this)
                } else {
                    val intent = Intent(this, AdminSignIn::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun signInSuccess(userInfo: Admin?) {
        StoreData.cAdmin = userInfo
        hideProgressDialog()
        startActivity(Intent(this, AdminActivity::class.java))
        finish()
    }

    fun signInFailed() {
        Toast.makeText(this, "Sign-In Failed", Toast.LENGTH_LONG).show()
        hideProgressDialog()
    }
}