package com.example.quitent1

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_uploadpdf.*

class uploadpdf : CommonData() {

    private lateinit var uri: Uri
    lateinit var mStorage: StorageReference
    var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadpdf)

        choose_pdf.setOnClickListener { view: View? ->
            onChooseClick(view)
        }

        upload_pdf.setOnClickListener { view: View? ->
            onUploadClick(view)
        }

        download_pdf.setOnClickListener {
            onDownloadClick()
        }
    }

    fun displayMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun onChooseClick(view: View?) {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select a PDF"), 0)
    }

    private fun onUploadClick(view: View?) {
        try {
            if (fileName.isEmpty()) {
                fileName = StoreData.cClass!!.classId
            }
            mStorage = FirebaseStorage.getInstance().getReference(StoreData.cClass!!.classSubCode)
            var mReference = mStorage.child(fileName)
            var progress = ProgressDialog(this)
            progress.setTitle("Uploading...")
            progress.show()
            mReference.putFile(uri).addOnSuccessListener {
                Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG).show()
                progress.setMessage("Please Wait Making An Entry...")
                StoreData.cClass!!.classPDF.add(fileName)
                FireStoreClass().writeBoardContent(this, storeData.cClass!!)
                progress.hide()
                finish()
            }.addOnFailureListener { it ->
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                progress.hide()
            }.addOnProgressListener { it ->
                val prog: Double = (100.0 * it.bytesTransferred) / it.totalByteCount
                progress.setMessage("Uploaded = ${String.format("%.2f", prog)}%")
                progress.show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun onDownloadClick() {
        try {
            if (fileName.isNotEmpty()) {
                fileName = StoreData.cClass!!.classId
            }
            mStorage = FirebaseStorage.getInstance().getReference(StoreData.cClass!!.classSubCode)
            val mReference = mStorage.child(fileName)
            val progress = ProgressDialog(this)
            progress.setTitle("Downloading...")
            progress.show()
            mReference.downloadUrl.addOnSuccessListener { it ->
                Toast.makeText(this, "Download successfully", Toast.LENGTH_LONG).show()
                progress.hide()
                val url = it.toString()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
                Toast.makeText(this, url, Toast.LENGTH_LONG).show()
            }.addOnFailureListener { it ->
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                progress.hide()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    uri = data.data!!
                    data.data?.let { returnUri ->
                        contentResolver.query(returnUri, null, null, null, null)
                    }?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        fileName = cursor.getString(nameIndex)
                    }
                    Toast.makeText(this, "Selected file $fileName", Toast.LENGTH_LONG).show()
                    pdf_image_no.visibility = View.GONE
                    pdf_image_yes.visibility = View.VISIBLE
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}