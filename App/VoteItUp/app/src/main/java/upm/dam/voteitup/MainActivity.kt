package upm.dam.voteitup

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import android.widget.ListView
import upm.dam.voteitup.entities.Poll
import android.widget.AdapterView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import upm.dam.voteitup.adapters.PollsListAdapter
import java.io.FileReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationViewEx>(R.id.bottom_navigation).apply {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
        };

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                R.id.action_search -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
                R.id.action_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            }

            true
        }

        var polls = mutableListOf<Poll>()

        Fuel
                .get("https://polar-oasis-43680.herokuapp.com/api/v1/polls")
                .header("Authorization" to "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.kHZQ03yhLOPC1c7f6CdItQbT2ljvMQLbucdJVkqwEKs")
                .responseJson { _, _, result ->
                    when (result) {
                        is Result.Failure -> {
                            Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG).show()
                        }
                        is Result.Success -> {
                            (0 until result.value.array().length()).mapTo(polls) { Gson().fromJson(result.value.array()[it].toString(), Poll::class.java) }

                            val listView = findViewById<ListView>(R.id.pollsListView)
                            listView.adapter = PollsListAdapter(this, polls)

                            // When the user clicks on the ListItem
                            listView.onItemClickListener = object : AdapterView.OnItemClickListener {

                                override fun onItemClick(a: AdapterView<*>, v: View, position: Int, id: Long) {
                                    val o = listView.getItemAtPosition(position)
                                    val poll = o as Poll
                                    //Toast.makeText(this@MainActivity, "Selected :" + " " + country, Toast.LENGTH_LONG).show()

                                    val intent = PollActivity.newIntent(baseContext, poll)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
    }
}
