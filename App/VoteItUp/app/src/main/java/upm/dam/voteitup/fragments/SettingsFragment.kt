package upm.dam.voteitup.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import upm.dam.voteitup.R
import upm.dam.voteitup.activities.LoginActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_settings, container, false)

        val sharedPreferences = activity.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        val TOKEN = getString(R.string.shared_preferences_token)

        val signOutButton = view!!.findViewById<Button>(R.id.signOutButton)

        signOutButton.setOnClickListener {
            val editor = sharedPreferences!!.edit()
            editor.remove(TOKEN)
            editor.apply()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity.finish()
        }

        return view;
    }
}
