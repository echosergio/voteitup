package upm.dam.voteitup.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_create_poll.*
import kotlinx.android.synthetic.main.activity_main.*
import upm.dam.voteitup.R
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.list_view_answers.view.*
import upm.dam.voteitup.R.string.Example_Answer
import upm.dam.voteitup.adapters.AnswerListAdapter


class CreatePollActivity : AppCompatActivity() {

    private lateinit var answersAdapter: AnswerListAdapter

    private lateinit var list: MutableList<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        var edittxt = EditText(this)
        edittxt.hint = Example_Answer.toString()
        list = mutableListOf( edittxt )
        answersAdapter = AnswerListAdapter(baseContext,
                listData = list)


        val listView = findViewById<ListView>(R.id.answerList)
        listView.adapter = answersAdapter

        plusAnswer.setOnClickListener { addNewAnswer() }
        answerList.onItemClickListener = AdapterView.OnItemClickListener { a, v, position, id ->
            val item = answersAdapter.getItem(position)
            answersAdapter.notifyDataSetChanged()
        }

    }

    private fun addNewAnswer() {
        //val listView = findViewById<ListView>(R.id.answerList)
        var edittxt = EditText(this)
        edittxt.hint = Example_Answer.toString()
        list.add(edittxt)
        answersAdapter.notifyDataSetChanged()

    }
}
