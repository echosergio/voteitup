package upm.dam.voteitup.activities

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_create_poll.*
import kotlinx.coroutines.experimental.async
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R
import upm.dam.voteitup.adapters.AnswerListAdapter
import upm.dam.voteitup.entities.Poll_POST
import java.util.ArrayList


class CreatePollActivity : AppCompatActivity() {

    private lateinit var answersAdapter: AnswerListAdapter

    private lateinit var list: MutableList<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        list = mutableListOf<TextView>()
        answersAdapter = AnswerListAdapter(baseContext,
                listData = list)
        val listView = findViewById<ListView>(R.id.answerList)
        listView.adapter = answersAdapter
        plusAnswer.setOnClickListener { addNewAnswer() }
        savePoll.setOnClickListener{ attmeptCreatePoll()}
        txtBox_desc.requestFocus()

    }

    private fun attmeptCreatePoll() {
        // Are valid
        if (!validatePoll().first) {
            validatePoll().second!!.requestFocus()
            return
        }

        // Get info
        val listAnswers = (list).filter{ editText ->
            editText.text.isNotBlank() && editText.text.isNotEmpty() }
                .map { it.text.toString()}
        val choices = mutableListOf<String>()
        listAnswers.forEach { choice ->
            choices.add(choice)
        }

        var poll = Poll_POST(text = txtBox_desc.text.toString(), choices = choices)

        // Save poll
        async { ApiClient.submitPool(poll) }
        finish()
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
        var edittxt = TextView(this)
        edittxt.text = text_add_new.text
        list.add(edittxt)
        answersAdapter.notifyDataSetChanged()
        text_add_new.setText("")
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState!!.putStringArrayList("list", (list).filter{ it ->
            it.text.isNotBlank() && it.text.isNotEmpty() }.map{it.text.toString()}
                as ArrayList<String>?)

    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        val listStrings =  savedInstanceState.getStringArrayList("list")

        listStrings.forEach(){
            var tv = TextView(this)
            tv.text=it
            list.add(tv)
            Log.d("Debug", "added" + it)
        }
        answersAdapter.notifyDataSetChanged()

    }


    /// <summary>
/// Make sure a list view item is within the visible area of the list view
/// and then select and set focus to it.
/// </summary>
/// <param name="itemIndex">index of item</param>

}
