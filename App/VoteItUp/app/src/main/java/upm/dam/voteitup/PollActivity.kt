package upm.dam.voteitup

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


class PollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poll)

        val horizontalBarChart = findViewById<HorizontalBarChart>(R.id.pollHorizontalBarChart)

        val choices = listOf(
                Choice(1, "Sí", 4234),
                Choice(2, "No", 3423))

        val pollBarChart = PollBarChart(choices)
        var typeFace: Typeface? = ResourcesCompat.getFont(this, R.font.roboto_light)
        pollBarChart.typeFace = typeFace
        val horizontalBarChart2 = pollBarChart.getChart(horizontalBarChart)

        horizontalBarChart2.invalidate()

        val lineChart = findViewById<LineChart>(R.id.pollActivityLineChart)

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
