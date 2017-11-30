package upm.dam.voteitup.charts

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import upm.dam.voteitup.entities.Choice


class PollBarChart constructor(private val choices: List<Choice>) {

    private val barEntries = mutableListOf<BarEntry>()
    private val barEntryMap = mutableMapOf<Float, Choice>()
    private val totalVotes = choices.sumBy { it.votes }

    var typeFace: Typeface? = null

    private val formatter = object : IAxisValueFormatter {
        val decimalDigits: Int
            get() = 0

        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            val votesPercentage = (barEntryMap[value]!!.votes * 100) / totalVotes.toFloat()
            val choiceText = barEntryMap[value]!!.text

            return choiceText + " (" + "%.1f".format(votesPercentage) + " %)"
        }
    }

    init {
        var xValue: Float = 0f;

        choices.forEach{ choice ->
            barEntries.add(BarEntry(xValue, choice.votes.toFloat()))
            barEntryMap.put(xValue, choice)
            xValue += 1f
        }
    }

    fun getChart(barChart: HorizontalBarChart): HorizontalBarChart {
        barChart.xAxis.granularity = 1f
        barChart.xAxis.valueFormatter = formatter
        barChart.xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
        barChart.xAxis.xOffset = 12f
        barChart.xAxis.setDrawAxisLine(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.textSize = 18f
        barChart.xAxis.textColor = Color.BLACK
        if (typeFace != null) barChart.xAxis.typeface = typeFace

        barChart.axisLeft.axisMaximum = totalVotes.toFloat()
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.isEnabled = false
        barChart.axisLeft.setDrawGridLines(false)

        barChart.axisRight.isEnabled = false
        barChart.axisRight.setDrawGridLines(false)

        barChart.description.isEnabled = false;
        barChart.legend.isEnabled = false;
        barChart.layoutParams.height = 200*choices.count()

        barChart.setDrawValueAboveBar(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)

        barChart.animateY(1000)
        barChart.setTouchEnabled(false)

        val barDataSet = BarDataSet(barEntries, null)
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList();
        barDataSet.valueTextSize = 18f
        barDataSet.setDrawValues(false);

        val barData = BarData(barDataSet)
        barChart.setData(barData)

        return barChart
    }
}