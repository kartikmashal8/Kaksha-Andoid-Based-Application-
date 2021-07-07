package com.example.quitent1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.Admin
import com.example.quitent1.model.Staff
import com.example.quitent1.model.student
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_flash.*

class FlashActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val user = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        SplashScreenImage!!.startAnimation(slideAnimation)

        val slideAnimation1 = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        splashsreentext!!.startAnimation(slideAnimation1)

        val slideAnimation2 = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        quientetImage!!.startAnimation(slideAnimation2)

        Handler().postDelayed({

            try {
                if (user != null) {
                    val fileName = "logininfo.txt"
                    var fileBody = ""
                    val context: Context = this
                    context.openFileInput(fileName).use { stream ->
                        fileBody = stream.bufferedReader().use {
                            it.readText()

                        }
                    }
                    if (fileBody.split("-")[0] == "890765") {
                        val id = fileBody.split("-")[1]
                        FireStoreClass().autoSignIn(this, id, 1)

                    } else if (fileBody.split("-")[0] == "457355") {
                        val id = fileBody.split("-")[1]
                        FireStoreClass().autoSignIn(this, id, 2)

                    } else if (fileBody.split("-")[0] == "6758909") {
                        val id = fileBody.split("-")[1]
                        FireStoreClass().autoSignIn(this, id, 3)

                    } else {
                        startActivity(Intent(this, IntroActivity::class.java))
                        finish()
                        val fileName = "logininfo.txt"
                        val fileBody = "786543-1"
                        val context: Context = this
                        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                            output.write(fileBody.toByteArray())
                        }

                    }
                } else {
                    val fileName = "logininfo.txt"
                    val fileBody = "786543-1"
                    val context: Context = this
                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                        output.write(fileBody.toByteArray())
                    }
                    startActivity(Intent(this, IntroActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

        }, 1200)
    }

    fun signInSuccessAdmin(data: Admin) {
        StoreData.cAdmin = data
        val dashboardIntent = Intent(this, AdminActivity::class.java)
        startActivity(dashboardIntent)
        finish()
    }

    fun signInSuccessStaff(data: Staff) {
        StoreData.cStaff = data
        val dashboardIntent = Intent(this, StaffActivity::class.java)
        startActivity(dashboardIntent)
        finish()
    }

    fun signInSuccessStud(data: student) {
        StoreData.cStud = data
        val dashboardIntent = Intent(this, StudentActivity::class.java)
        startActivity(dashboardIntent)
        finish()
    }


    fun signInFailed() {
        Toast.makeText(this, "", Toast.LENGTH_LONG).show()
    }
}