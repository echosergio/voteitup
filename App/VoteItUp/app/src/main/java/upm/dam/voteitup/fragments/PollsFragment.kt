package upm.dam.voteitup.fragments

import android.content.Intent.getIntent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import kotlinx.android.synthetic.main.fragment_polls.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R
import upm.dam.voteitup.activities.PollsActivity
import upm.dam.voteitup.adapters.PollsListAdapter
import upm.dam.voteitup.entities.Poll
import upm.dam.voteitup.entities.PollActivity
import java.text.SimpleDateFormat
import java.util.*

class PollsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_polls, container, false)

        val listView = view!!.findViewById<ListView>(R.id.pollsListView)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = PollsActivity.newIntent(activity, listView.getItemAtPosition(position) as Poll)
            startActivity(intent)
        }

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -6)
        val date = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)

        val getPollsAsync = async { ApiClient.getPolls() }

        launch(UI) {
            val polls = getPollsAsync.await()!!.sortedByDescending { it.id!!.toInt() }

            listView.adapter = PollsListAdapter(activity, polls)

            val mainPoll = polls.filter { it.creationDate!! > date }.maxBy { it.Choices!!.sumBy { it.votes } }
            mainPollTextView.text = mainPoll!!.text
            mainPollVotesTextView.text = mainPoll.Choices!!.sumBy { it.votes }.toString() + " votos"

            mainPollLayout.setOnClickListener {
                val intent = PollsActivity.newIntent(activity, mainPoll)
                startActivity(intent)
            }
        }

        val simpleSearchView = view.findViewById<SearchView>(R.id.searchView)

        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String): Boolean {
                val getPollsAsync = async { ApiClient.getPolls(keyword) }

                launch(UI) {
                    val polls = getPollsAsync.await()!!.sortedByDescending { it.id!!.toInt() }
                    mainPollLayout.visibility = View.GONE
                    listView.adapter = PollsListAdapter(activity, polls)
                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        simpleSearchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                val getPollsAsync = async { ApiClient.getPolls() }

                launch(UI) {
                    val polls = getPollsAsync.await()!!.sortedByDescending { it.id!!.toInt() }

                    listView.adapter = PollsListAdapter(activity, polls)

                    mainPollLayout.visibility = View.VISIBLE

                    val mainPoll = polls.filter { it.creationDate!! > date }.maxBy { it.Choices!!.sumBy { it.votes } }
                    mainPollTextView.text = mainPoll!!.text
                    mainPollVotesTextView.text = mainPoll.Choices!!.sumBy { it.votes }.toString() + " votos"

                    mainPollLayout.setOnClickListener {
                        val intent = PollsActivity.newIntent(activity, mainPoll)
                        startActivity(intent)
                    }
                }

                return false;
            }
        });

        return view;
    }
}
