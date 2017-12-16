package upm.dam.voteitup.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient

import upm.dam.voteitup.R
import upm.dam.voteitup.activities.PollActivity
import upm.dam.voteitup.adapters.PollsListAdapter
import upm.dam.voteitup.entities.Poll

class PollsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_polls, container, false)

        val listView = view!!.findViewById<ListView>(R.id.pollsListView)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = PollActivity.newIntent(activity, listView.getItemAtPosition(position) as Poll)
            startActivity(intent)
        }

        listView.adapter = PollsListAdapter(activity, listOf<Poll>())

        val getPollsAsync = async { ApiClient.getPolls() }
        launch(UI) { listView.adapter = PollsListAdapter(activity, getPollsAsync.await()!!) }

        val simpleSearchView = view!!.findViewById<SearchView>(R.id.searchView)

        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String): Boolean {
                val getPollsAsync = async { ApiClient.getPolls(keyword) }
                launch(UI) { listView.adapter = PollsListAdapter(activity, getPollsAsync.await()!!) }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        return view;
    }
}