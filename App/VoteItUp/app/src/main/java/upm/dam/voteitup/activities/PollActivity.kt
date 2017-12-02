package upm.dam.voteitup.activities

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
import android.content.Intent
import upm.dam.voteitup.entities.Poll
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R

class PollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poll)

        val pollId = intent.getStringExtra(INTENT_POLL_ID)
                ?: throw IllegalStateException("field ${INTENT_POLL_ID} missing in Intent")

        val horizontalBarChart = findViewById<HorizontalBarChart>(R.id.pollHorizontalBarChart)
        val lineChart = findViewById<LineChart>(R.id.pollActivityLineChart)

        val getPollsAsync = async { ApiClient.getPoll(pollId.toInt()) }

        launch(UI) {
            val poll = getPollsAsync.await()!!

            val pollText = findViewById<TextView>(R.id.pollText)
            pollText.text = poll.text

            val votesText = findViewById<TextView>(R.id.votesText)
            votesText.text = poll.Choices.toString()
            votesText.text = poll.Choices.orEmpty().sumBy { it.votes }.toString() + " votos"

            val choices = poll.Choices

            val pollBarChart = PollBarChart(choices!!)
            var typeFace: Typeface? = ResourcesCompat.getFont(baseContext, R.font.roboto_light)
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

    companion object {

        private val INTENT_POLL_ID = "poll_id"

        fun newIntent(context: Context, poll: Poll): Intent {
            val intent = Intent(context, PollActivity::class.java)
            intent.putExtra(INTENT_POLL_ID, poll.id)
            return intent
        }
    }
}
