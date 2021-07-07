package com.example.quitent1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.quitent1.model.QuizModel
import com.example.quitent1.utils.Constants
import kotlinx.android.synthetic.main.activity_board_content.*

class BoardContent : CommonData() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_content)

        board_content_announcement.setOnClickListener {
            onClickAnnouncment()
        }

        board_content_notes.setOnClickListener {
            onClickNotes()
        }

        board_content_quizes.setOnClickListener {
            onClickQuiz()
        }

        board_content_add_announcement.setOnClickListener {
            val intent = Intent(this, boardComment::class.java)
            startActivity(intent)
        }

        board_content_add_notes.setOnClickListener {
            val intent = Intent(this, uploadpdf::class.java)
            startActivity(intent)
        }

        board_content_add_quiz.setOnClickListener {
            val intent = Intent(this, CreateQuiz::class.java)
            startActivity(intent)
        }

        board_content_quiz_marks.setOnItemClickListener { adapterView, view, i, l ->
            val temp = adapterView.adapter.getItem(i) as QuizModel
            storeData.cQuiz = temp
            val intent = Intent(this, StaffQuizMarks::class.java)
            startActivity(intent)
        }
    }


    private fun onClickAnnouncment() {
        board_content_add_announcement.visibility = View.VISIBLE
        board_content_add_notes.visibility = View.GONE
        board_content_add_quiz.visibility = View.GONE

        try {
            if (storeData.cClass != null) {
                if (storeData.cClass!!.classNotice.size > 1) {
                    board_content_list.visibility = View.VISIBLE
                    board_content_message.visibility = View.GONE
                    board_content_quiz_marks.visibility = View.GONE
                    val arr: ArrayList<String> =
                        storeData.cClass!!.classNotice.reversed() as ArrayList<String>
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
                    board_content_list.adapter = adapter
                } else {
                    annoucmentVisibility()
                }
            } else {
                annoucmentVisibility()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            board_content_message.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun annoucmentVisibility() {
        board_content_message.text = "No Announcement yet"
        board_content_quiz_marks.visibility = View.GONE
        board_content_list.visibility = View.GONE
        board_content_message.visibility = View.VISIBLE
    }

    private fun onClickNotes() {
        board_content_add_announcement.visibility = View.GONE
        board_content_add_notes.visibility = View.VISIBLE
        board_content_add_quiz.visibility = View.GONE

        try {
            if (storeData.cClass != null) {
                if (storeData.cClass!!.classPDF.size > 1) {
                    try {
                        board_content_list.visibility = View.VISIBLE
                        board_content_message.visibility = View.GONE
                        board_content_quiz_marks.visibility = View.GONE
                        var arr: ArrayList<String> =
                            storeData.cClass!!.classPDF.reversed() as ArrayList<String>
                        val adapter: ArrayAdapter<String> =
                            ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
                        board_content_list.adapter = adapter
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
        board_content_message.text = "No Notes added yet"
        board_content_quiz_marks.visibility = View.GONE
        board_content_message.visibility = View.VISIBLE
        board_content_list.visibility = View.GONE
    }

    private fun onClickQuiz() {
        board_content_add_announcement.visibility = View.GONE
        board_content_add_notes.visibility = View.GONE
        board_content_add_quiz.visibility = View.VISIBLE

        try {
            if (storeData.cClass != null) {
                if (storeData.cClass!!.classQuiz.size > 1) {
                    try {
                        board_content_list.visibility = View.GONE
                        board_content_message.visibility = View.GONE
                        board_content_quiz_marks.visibility = View.VISIBLE
                        mFireStore.collection(Constants.quizzes).get()
                            .addOnSuccessListener { document ->
                                val quizList: ArrayList<QuizModel> = ArrayList()
                                for (i in document.documents) {
                                    if (i.id in storeData.cClass!!.classQuiz.keys) {
                                        val quizTemp = i.toObject(QuizModel::class.java)!!
                                        quizList.add(quizTemp)
                                    }
                                }
                                val adapter =
                                    LoadQuiz(this, R.layout.quiz_btn_shape, quizList)
                                board_content_quiz_marks.adapter = adapter
                            }
//                        val arr: ArrayList<String> = ArrayList()
//                        for (i in StoreData.cClass!!.classQuiz) {
//                            arr.add(i.value)
//                        }
//                        val adapter: ArrayAdapter<String> =
//                            ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
//                        board_content_list.adapter = adapter
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
        board_content_message.text = "No Quizzes added yet"
        board_content_list.visibility = View.GONE
        board_content_message.visibility = View.VISIBLE
        board_content_list.visibility = View.GONE
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
