package upm.dam.voteitup.activities

import android.content.Intent
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_create_poll.*
import kotlinx.coroutines.experimental.async
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R
import upm.dam.voteitup.R.string.Example_Answer
import upm.dam.voteitup.adapters.AnswerListAdapter
import upm.dam.voteitup.entities.Choice
import upm.dam.voteitup.entities.Poll


class CreatePollActivity : AppCompatActivity() {

    private lateinit var answersAdapter: AnswerListAdapter

    private lateinit var list: MutableList<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        list = mutableListOf<EditText>()
        answersAdapter = AnswerListAdapter(baseContext,
                listData = list)
        val listView = findViewById<ListView>(R.id.answerList)
        listView.adapter = answersAdapter

        plusAnswer.setOnClickListener { addNewAnswer() }
        savePoll.setOnClickListener{ attmeptCreatePoll()}
        answerList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item = answersAdapter.getItem(position)
            answersAdapter.notifyDataSetChanged()
        }
        txtBox_desc.requestFocus()

    }

    private fun attmeptCreatePoll() {
        //areValid
        if (!validatePoll().first) {
            validatePoll().second!!.requestFocus()
            return
        }
        //get info
        val listAnswers = (list).filterNot{ editText ->
            editText.text.isNotBlank()
                    && editText.text.isNotEmpty() }
                .map { it.text.toString()}
        val choices = mutableListOf<Choice>()
        var i = 0
        listAnswers.forEach { choice ->
            choices.add(Choice(id= i , text = choice) )
            i++
        }

        var poll = Poll(text = txtBox_desc.text.toString(),
                        UserId = "1",
                        Choices = choices,
                        id ="1")

        //save pull.
        val result = async { ApiClient.submitPool(poll) }

        val intent = Intent(this, PollActivity::class.java)
        intent.putExtra(PollActivity.INTENT_POLL_ID, "1")
        startActivity(intent)
    }

    private fun validatePoll(): Pair<Boolean,View?> {

        // Reset errors.
        txtBox_desc.error = null
        tv_AnwserLbl.error = null

        // Store values at the time of the login attempt.
        val descStr = txtBox_desc.text.toString()
        val listAnswers = (list).filter{ editText ->
            editText.text.isNotBlank()
            && editText.text.isNotEmpty() }

        var cancel = false
        var focusView: View? = null

        // Check for a valid description
        if (TextUtils.isEmpty(descStr)) {
            txtBox_desc.error = getString(R.string.error_desc_empty)
            focusView = txtBox_desc
            cancel = true
        }

        // Check for a valid email address.
        if (listAnswers.size < 2) {
            tv_AnwserLbl.error = getString(R.string.error_two_answers_needed)
            focusView = tv_AnwserLbl
            cancel = true
        }
        return Pair(!cancel,focusView)
    }

    private fun addNewAnswer() {
        val listView = findViewById<ListView>(R.id.answerList)
        listView.requestFocus()
        if(list.count() == 0){
            var edittxt = EditText(this)
            edittxt.hint = Example_Answer.toString()
            edittxt.isSelected = true
            edittxt.requestFocus()
            list.add(edittxt)
            answersAdapter.notifyDataSetChanged()

        }
        var edittxt = EditText(this)
        edittxt.hint = Example_Answer.toString()
        edittxt.isSelected = true
        edittxt.requestFocus()
        list.add(edittxt)
        answersAdapter.notifyDataSetChanged()

    }

    /// <summary>
/// Make sure a list view item is within the visible area of the list view
/// and then select and set focus to it.
/// </summary>
/// <param name="itemIndex">index of item</param>

}
