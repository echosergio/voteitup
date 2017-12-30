package upm.dam.voteitup.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_profile, container, false)

        val getCurrentUserAsync = async { ApiClient.getCurrentUser() }

        launch(UI) {
            val user = getCurrentUserAsync.await() ?: error("Error retrieving User info")

            val usernameTextView = view!!.findViewById<TextView>(R.id.usernameTextView)
            usernameTextView.text = user.username

            val bioTextView = view.findViewById<TextView>(R.id.bioTextView)
            bioTextView.text = user.bio
        }

        return view;
    }
}
