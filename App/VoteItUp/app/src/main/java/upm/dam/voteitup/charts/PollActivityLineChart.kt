package upm.dam.voteitup.charts

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import upm.dam.voteitup.entities.Activity

class PollActivityLineChart constructor(activities: List<Activity>) {

    private val entries = mutableListOf<Entry>()

    init {
        var xValue: Float = 0f;

        activities.forEach{ activity ->
            entries.add(Entry(xValue, activity.votes.toFloat()))
            xValue += 1f
        }
    }

    fun getChart(lineChart: LineChart): LineChart {
        lineChart.xAxis.isEnabled = false
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        //lineChart.xAxis.setLabelCount(activities.count(),true)

        lineChart.axisLeft.isEnabled = false
        lineChart.axisLeft.setDrawGridLines(false)

        lineChart.axisRight.isEnabled = false
        lineChart.axisRight.setDrawGridLines(false)

        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.layoutParams.height = 500

        lineChart.setDrawGridBackground(false);

        lineChart.animateY(1000);
        lineChart.setTouchEnabled(false)

        val lineDataSet = LineDataSet(entries, null)
        lineDataSet.setDrawFilled(true);
        lineDataSet.circleRadius = 5f
        lineDataSet.lineWidth = 3f

        val colorf = Color.rgb(101, 200, 237)
        lineDataSet.color = colorf;
        lineDataSet.setCircleColor(colorf)
        lineDataSet.fillColor = colorf

        lineDataSet.highLightColor = Color.BLUE

        val lineData = LineData(lineDataSet)

        lineChart.data = lineData

        return lineChart
    }
}