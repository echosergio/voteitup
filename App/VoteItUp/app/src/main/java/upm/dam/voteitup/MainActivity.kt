package upm.dam.voteitup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import android.R.attr.entries
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.Legend




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val horizontalBarChart = findViewById<HorizontalBarChart>(R.id.horizontalBarChart)
        // Look-up table

// Set the value formatter



        val quarters = arrayOf("Q1", "Q2")

        val formatter = object : IAxisValueFormatter {

            // we don't draw numbers, so no decimal digits needed
            val decimalDigits: Int
                get() = 0

            override fun getFormattedValue(value: Float, axis: AxisBase): String {
                return quarters[value.toInt()]
            }
        }

        val xAxis = horizontalBarChart.getXAxis()
        xAxis.setGranularity(1f) // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter)

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        var typeFace: Typeface? = ResourcesCompat.getFont(this, R.font.pacifico)
        xAxis.typeface = typeFace
        xAxis.textSize = 18f
        xAxis.textColor = Color.rgb(39, 64, 104)

        val entries = listOf(
                BarEntry(0f, 4f),
        BarEntry(1f, 65f)
        )




        val set = BarDataSet(entries, "BarDataSet")
        set.setColor(Color.rgb(39, 64, 104));

        // Set the maximum value that can be taken by the bars
        horizontalBarChart.getAxisLeft().setAxisMaximum(100f);
        horizontalBarChart.getAxisLeft().setAxisMinimum(0f);

        // Display scores inside the bars
        horizontalBarChart.setDrawValueAboveBar(true);
        horizontalBarChart.setDrawBarShadow(false);
        horizontalBarChart.setDrawGridBackground(false)
        horizontalBarChart.setDrawBorders(false)


        horizontalBarChart.getAxisLeft().setEnabled(false);
        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.getDescription().setEnabled(false);
        horizontalBarChart.getLegend().setEnabled(false);


        horizontalBarChart.getXAxis().setDrawAxisLine(false);
        horizontalBarChart.getXAxis().setDrawGridLines(false);
        horizontalBarChart.getAxisLeft().setDrawGridLines(false);
        horizontalBarChart.getAxisRight().setDrawGridLines(false);

        val data = BarData(set)
        data.setValueTypeface(typeFace)

        horizontalBarChart.setData(data)
        horizontalBarChart.setFitBars(true) // make the x-axis fit exactly all bars
        horizontalBarChart.invalidate() // refresh

    }
}
