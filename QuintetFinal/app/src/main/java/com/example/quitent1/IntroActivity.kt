package com.example.quitent1

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.quintet_anim)
        quienteticon!!.startAnimation(slideAnimation)

        val slideAnimation1 = AnimationUtils.loadAnimation(this, R.anim.quintet_anim)
        letsgetstartedtext!!.startAnimation(slideAnimation1)


        intro_admin.setOnClickListener {
            startActivity(Intent(this, AdminIntro::class.java))
            finish()
        }

        intro_staff.setOnClickListener {
            startActivity(Intent(this, StaffIntro::class.java))
            finish()
        }

        intro_student.setOnClickListener {
            startActivity(Intent(this, StudentIntro::class.java))
            finish()
        }

    }

}