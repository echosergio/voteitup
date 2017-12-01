package upm.dam.voteitup

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import upm.dam.voteitup.entities.Poll
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import upm.dam.voteitup.adapters.PollsListAdapter
import java.io.FileReader
import android.support.v4.widget.SearchViewCompat.setOnQueryTextListener
import android.widget.*


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
                R.id.action_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.action_search -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
                R.id.action_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            }

            true
        }

        var polls = mutableListOf<Poll>()

        val simpleSearchView = findViewById<SearchView>(R.id.searchView) // inititate a search view
        val listView = findViewById<ListView>(R.id.pollsListView)
        var imageCoverView = findViewById<ImageView>(R.id.imageCoverView);

        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String): Boolean {

                imageCoverView.visibility = View.GONE
                polls.clear()

                Fuel
                        .get("https://polar-oasis-43680.herokuapp.com/api/v1/polls?keyword=" + keyword)
                        .header("Authorization" to "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.kHZQ03yhLOPC1c7f6CdItQbT2ljvMQLbucdJVkqwEKs")
                        .responseJson { _, _, result ->
                            when (result) {
                                is Result.Failure -> {
                                    Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG).show()
                                }
                                is Result.Success -> {
                                    (0 until result.value.array().length()).mapTo(polls) { Gson().fromJson(result.value.array()[it].toString(), Poll::class.java) }

                                    listView.adapter = PollsListAdapter(baseContext, polls)
                                    listView.onItemClickListener = AdapterView.OnItemClickListener { a, v, position, id ->
                                        val o = listView.getItemAtPosition(position)
                                        val poll = o as Poll
                                        val intent = PollActivity.newIntent(baseContext, poll)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

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

                            listView.adapter = PollsListAdapter(this, polls)
                            listView.onItemClickListener = AdapterView.OnItemClickListener { a, v, position, id ->
                                val o = listView.getItemAtPosition(position)
                                val poll = o as Poll
                                val intent = PollActivity.newIntent(baseContext, poll)
                                startActivity(intent)
                            }
                        }
                    }
                }
    }
}
