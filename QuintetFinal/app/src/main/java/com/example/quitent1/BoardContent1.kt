package com.example.quitent1

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.quitent1.model.QuizModel
import com.example.quitent1.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_board_content1.*

class BoardContent1 : CommonData() {

    var fileName: String = ""
    lateinit var mStorage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_content1)

        board1_content_announcement.setOnClickListener {
            onClickAnnouncment()
        }

        board1_content_notes.setOnClickListener {
            onClickNotes()
        }

        board1_content_quizes.setOnClickListener {
            onClickQuiz()
        }

        board1_content_list.setOnItemClickListener { adapterView, view, i, l ->
            val temp = adapterView.adapter.getItem(i) as String
            fileName = temp
            onDownloadClick()
        }

        board3_content_list.setOnItemClickListener { adapterView, view, i, l ->
            val temp = adapterView.adapter.getItem(i) as QuizModel
            storeData.cQuiz = temp
            if (check(temp)) {
                val intent = Intent(this, StartQuiz::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Already attempted", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun check(data: QuizModel): Boolean {
        return StoreData.cStud!!.studUSN !in data.qMarks.keys
    }

    private fun onClickAnnouncment() {
        try {
            if (storeData.cClass != null) {
                if (storeData.cClass!!.classNotice.size > 1) {
                    board2_content_list.visibility = View.VISIBLE
                    board1_content_list.visibility = View.GONE
                    board3_content_list.visibility = View.GONE
                    board1_content_message.visibility = View.GONE
                    val arr: ArrayList<String> =
                        storeData.cClass!!.classNotice.reversed() as ArrayList<String>
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
                    board2_content_list.adapter = adapter
                } else {
                    annoucmentVisibility()
                }
            } else {
                annoucmentVisibility()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            annoucmentVisibility()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun annoucmentVisibility() {
        board1_content_message.text = "No Announcements yet"
        board2_content_list.visibility = View.GONE
        board1_content_list.visibility = View.GONE
        board3_content_list.visibility = View.GONE
        board1_content_message.visibility = View.VISIBLE
    }

    private fun onClickNotes() {

        try {
            if (storeData.cClass != null) {
                if (storeData.cClass!!.classPDF.size > 1) {
                    try {
                        board1_content_list.visibility = View.VISIBLE
                        board2_content_list.visibility = View.GONE
                        board3_content_list.visibility = View.GONE
                        board1_content_message.visibility = View.GONE
                        var arr: ArrayList<String> =
                            storeData.cClass!!.classPDF.reversed() as ArrayList<String>
                        val adapter: ArrayAdapter<String> =
                            ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
                        board1_content_list.adapter = adapter
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    notesVisibility()
                }
            } else {
                notesVisibility()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            notesVisibility()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun notesVisibility() {
        board1_content_message.text = "No Notes added yet"
        board1_content_message.visibility = View.VISIBLE
        board1_content_list.visibility = View.GONE
        board2_content_list.visibility = View.GONE
        board3_content_list.visibility = View.GONE
    }

    private fun onClickQuiz() {

        try {
            if (storeData.cClass != null) {
                if (storeData.cClass!!.classQuiz.size > 1) {
                    try {
                        board3_content_list.visibility = View.VISIBLE
                        board2_content_list.visibility = View.GONE
                        board1_content_list.visibility = View.GONE
                        board1_content_message.visibility = View.GONE
                        mFireStore.collection(Constants.quizzes).get()
                            .addOnSuccessListener { document ->
                                val quizList: ArrayList<QuizModel> = ArrayList()
                                for (i in document.documents) {
                                    if (i.id in storeData.cClass!!.classQuiz.keys) {
                                        val quizTemp = i.toObject(QuizModel::class.java)!!
                                        quizList.add(quizTemp)
                                    }
                                }
                                val adapter = LoadQuiz(this, R.layout.quiz_btn_shape, quizList)
                                board3_content_list.adapter = adapter
                            }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    quizVisibility()
                }
            } else {
                quizVisibility()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            quizVisibility()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun quizVisibility() {
        board1_content_message.text = "No Quizzes added yet"
        board1_content_message.visibility = View.VISIBLE
        board1_content_list.visibility = View.GONE
        board2_content_list.visibility = View.GONE
        board3_content_list.visibility = View.GONE
    }

    private fun onDownloadClick() {
        try {
            if (fileName.isEmpty()) {
                fileName = StoreData.cClass!!.classId
            }
            mStorage = FirebaseStorage.getInstance().getReference(StoreData.cClass!!.classSubCode)
            val mReference = mStorage.child(fileName)
            val progress = ProgressDialog(this)
            progress.setTitle("Downloading...")
            progress.show()
            mReference.downloadUrl.addOnSuccessListener {
                Toast.makeText(this, "Download successfully", Toast.LENGTH_LONG).show()
                progress.hide()
                val url = it.toString()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                progress.hide()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    class LoadQuiz(var mCtx: Context, var resources: Int, var items: List<QuizModel>) :
        ArrayAdapter<QuizModel>(mCtx, resources, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutIn: LayoutInflater = LayoutInflater.from(mCtx)
            val vw: View = layoutIn.inflate(resources, null)

            val qName: TextView = vw.findViewById(R.id.quiz_board_name)
            val oClass: QuizModel = items[position]

            qName.text = oClass.qName

            return vw
        }
    }


}