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
import upm.dam.voteitup.adapters.PollsListAdapter

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
                R.id.action_trending -> Toast.makeText(this, "Trending", Toast.LENGTH_SHORT).show()
                R.id.action_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }

            true
        }

        val tom = Poll(text = "#Referendum de independecia #Cataluña", creationDate = "125167 votos")
        val jerry = Poll(text = "#HuelgaDocentes", creationDate = "32427 votos")
        val donald = Poll(text = "#Elecciones 21D", creationDate = "65926 votos")

        val users = listOf<Poll>(tom, jerry, donald)


        val listView = findViewById<ListView>(R.id.pollsListView)
        listView.adapter = PollsListAdapter(this, users)

        // When the user clicks on the ListItem
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(a: AdapterView<*>, v: View, position: Int, id: Long) {
                val o = listView.getItemAtPosition(position)
                val country = o as Poll
                //Toast.makeText(this@MainActivity, "Selected :" + " " + country, Toast.LENGTH_LONG).show()

                val intent = Intent(baseContext, PollActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
