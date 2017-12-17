package upm.dam.voteitup.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import upm.dam.voteitup.R
import upm.dam.voteitup.fragments.PollsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            val intent = Intent(this, CreatePollActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationViewEx>(R.id.bottom_navigation).apply {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
        };

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.mainFragment, PollsFragment()).commit()
                }
                R.id.action_search -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
                R.id.action_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            }

            true
        }

        if (mainFragment != null) {
            if (savedInstanceState != null) {
                return
            }

            supportFragmentManager.beginTransaction().add(R.id.mainFragment, PollsFragment()).commit()
        }
    }
}
