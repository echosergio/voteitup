package upm.dam.voteitup.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import upm.dam.voteitup.entities.Poll
import upm.dam.voteitup.adapters.PollsListAdapter
import android.widget.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R


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

        val listView = findViewById<ListView>(R.id.pollsListView)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = PollActivity.newIntent(baseContext, listView.getItemAtPosition(position) as Poll)
            startActivity(intent)
        }

        val getPollsAsync = async { ApiClient.getPolls() }
        launch(UI) { listView.adapter = PollsListAdapter(baseContext, getPollsAsync.await()!!) }

        val simpleSearchView = findViewById<SearchView>(R.id.searchView)

        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String): Boolean {
                val getPollsAsync = async { ApiClient.getPolls(keyword) }
                launch(UI) { listView.adapter = PollsListAdapter(baseContext, getPollsAsync.await()!!) }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }
}
