package upm.dam.voteitup.charts

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import upm.dam.voteitup.R


class PollBarChart constructor(val barChart: HorizontalBarChart, val choiceMap: HashMap<String, Int>) {

    private val entries = mutableListOf<BarEntry>()
    private val entriesMap = mutableMapOf<Float, String>()

    var typeFace: Typeface? = null

    private val totalVotes = choiceMap.values.sum().toFloat()

    private val formatter = object : IAxisValueFormatter {
        val decimalDigits: Int
            get() = 0

        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            val votesPercentage = (choiceMap[entriesMap[value]]!! * 100) / totalVotes
            val choiceText = entriesMap[value]

            return choiceText + " (" + "%.1f".format(votesPercentage) + " %)"
        }
    }

    init {
        var xValue: Float = 0f;

        for (choiceText: String in choiceMap.keys) {
            entriesMap.put(xValue, choiceText)
            entries.add(BarEntry(xValue, choiceMap.get(choiceText)!!.toFloat()))
            xValue += 1f
        }
    }

    fun g(): HorizontalBarChart {
        val xAxis = barChart.getXAxis()
        xAxis.setGranularity(1f) // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter)

        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setXOffset(12f);


        xAxis.typeface = typeFace
        xAxis.textSize = 18f
        xAxis.textColor = Color.BLACK

        val set = BarDataSet(entries, "BarDataSet")
        set.setColors(ColorTemplate.COLORFUL_COLORS.toList());
        set.valueTextSize = 18f
        set.setDrawValues(false);

        barChart.setTouchEnabled(false)

        // Set the maximum value that can be taken by the bars
        barChart.getAxisLeft().setAxisMaximum(totalVotes);
        barChart.getAxisLeft().setAxisMinimum(0f);

        // Display scores inside the bars

        barChart.setDrawValueAboveBar(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)


        barChart.getAxisLeft().setEnabled(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);


        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.animateY(1000);

        barChart.getLayoutParams().height=200*choiceMap.keys.count();

        val data = BarData(set)
        barChart.setData(data)

        return barChart
    }
}