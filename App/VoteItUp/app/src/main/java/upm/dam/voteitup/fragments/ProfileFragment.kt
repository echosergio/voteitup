package upm.dam.voteitup.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R
import upm.dam.voteitup.activities.PollsActivity
import upm.dam.voteitup.adapters.UserActivityListAdapter
import upm.dam.voteitup.entities.Poll
import upm.dam.voteitup.entities.UserActivity
import upm.dam.voteitup.views.NonScrollListView

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_profile, container, false)

        val currentUserId = ApiClient.getCurrentUserId();

        val getCurrentUserAsync = async { ApiClient.getUser(currentUserId) }

        launch(UI) {
            val user = getCurrentUserAsync.await() ?: error("Error retrieving User info")

            val usernameTextView = view!!.findViewById<TextView>(R.id.usernameTextView)
            usernameTextView.text = user.username

            val bioTextView = view.findViewById<TextView>(R.id.bioTextView)
            bioTextView.text = user.bio
        }

        val listView = view!!.findViewById<NonScrollListView>(R.id.userActivityListView)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = PollsActivity.newIntent(activity, listView.getItemAtPosition(position) as UserActivity)
            startActivity(intent)
        }

        listView.adapter = UserActivityListAdapter(activity, listOf<UserActivity>())

        val getUserActivityAsync = async { ApiClient.getUserActivity(currentUserId) }
        launch(UI) { listView.adapter = UserActivityListAdapter(activity, getUserActivityAsync.await()!!) }

        return view;
    }
}
