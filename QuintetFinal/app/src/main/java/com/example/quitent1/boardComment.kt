package com.example.quitent1

import android.os.Bundle
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import kotlinx.android.synthetic.main.activity_board_comment.*

class boardComment : CommonData() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_comment)

        board_comment_submit.setOnClickListener {
            onSubmit()
        }
    }

    fun displayMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
        finish()
    }

    private fun onSubmit() {
        try {
            showProgressDialog(resources.getString(R.string.please_wait))
            val message: String = board_comment_message.text.toString()
            storeData.cClass!!.classNotice.add(message)
            FireStoreClass().writeBoardContent(this, storeData.cClass!!)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            hideProgressDialog()
        }
    }
}