package upm.dam.voteitup

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.HorizontalBarChart
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import upm.dam.voteitup.charts.PollBarChart
import upm.dam.voteitup.charts.PollActivityLineChart
import upm.dam.voteitup.entities.Activity
import upm.dam.voteitup.entities.Choice
import android.content.Intent
import upm.dam.voteitup.entities.Poll
import android.graphics.Movie
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import upm.dam.voteitup.adapters.PollsListAdapter


class PollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poll)

        val pollId = intent.getStringExtra(INTENT_POLL_ID)
                ?: throw IllegalStateException("field $INTENT_POLL_ID missing in Intent")

        val horizontalBarChart = findViewById<HorizontalBarChart>(R.id.pollHorizontalBarChart)
        val lineChart = findViewById<LineChart>(R.id.pollActivityLineChart)

        Fuel
                .get("https://polar-oasis-43680.herokuapp.com/api/v1/polls/" + pollId)
                .header("Authorization" to "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.kHZQ03yhLOPC1c7f6CdItQbT2ljvMQLbucdJVkqwEKs")
                .responseJson { _, _, result ->
                    when (result) {
                        is Result.Failure -> {
                            Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
                        }
                        is Result.Success -> {
                            var poll = Gson().fromJson(result.value.obj().toString(), Poll::class.java)

                            val pollText = findViewById<TextView>(R.id.pollText)
                            pollText.text = poll.text

                            val votesText = findViewById<TextView>(R.id.votesText)
                            votesText.text = poll.Choices.toString()
                            votesText.text = poll.Choices.orEmpty().sumBy { it.votes }.toString() + " votos"

                            val choices = poll.Choices

                            val pollBarChart = PollBarChart(choices!!)
                            var typeFace: Typeface? = ResourcesCompat.getFont(this, R.font.roboto_light)
                            pollBarChart.typeFace = typeFace
                            val horizontalBarChart2 = pollBarChart.getChart(horizontalBarChart)

                            horizontalBarChart2.invalidate()

                            val activities = mutableListOf(
                                    Activity(134534, "01-11-2017"),
                                    Activity(523325, "01-11-2017"),
                                    Activity(233225, "01-11-2017"),
                                    Activity(423434, "01-11-2017"),
                                    Activity(323424, "01-11-2017"),
                                    Activity(654645, "01-11-2017"),
                                    Activity(764564, "01-11-2017"))

                            val pollActivityLineChart = PollActivityLineChart(activities)
                            val lineChart2 = pollActivityLineChart.getChart(lineChart)

                            lineChart2.invalidate()
                        }
                    }
                }
    }

    companion object {

        private val INTENT_POLL_ID = "poll_id"

        fun newIntent(context: Context, poll: Poll): Intent {
            val intent = Intent(context, PollActivity::class.java)
            intent.putExtra(INTENT_POLL_ID, poll.id)
            return intent
        }
    }
}
