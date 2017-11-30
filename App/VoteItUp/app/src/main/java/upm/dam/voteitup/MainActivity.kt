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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.ColorTemplate
import upm.dam.voteitup.charts.PollBarChart
import java.util.stream.Collectors.toList
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val horizontalBarChart = findViewById<HorizontalBarChart>(R.id.horizontalBarChart)

        val choicesMap = hashMapOf("Sí" to 224242, "No" to 423443)
//replace with choices objects
        val c = PollBarChart(horizontalBarChart, choicesMap)
        var typeFace: Typeface? = ResourcesCompat.getFont(this, R.font.arvo)
        c.typeFace = typeFace
        val horizontalBarChart2 = c.g()

        horizontalBarChart2.invalidate() // refresh


        val lineChart = findViewById<LineChart>(R.id.lineChart)

        val entries1 = mutableListOf(
                Entry(0f, 100000f),
                Entry(1f, 140000f),
                Entry(2f, 170000f),
                Entry(3f, 150000f),
                Entry(4f, 180000f),
                Entry(5f, 190000f),
                Entry(6f, 130000f))

        val setComp1 = LineDataSet(entries1, "Company 1")
        setComp1.axisDependency = AxisDependency.LEFT


        val dataset = LineDataSet(entries1, null)
        dataset.setDrawFilled(true);
        dataset.setCircleRadius(5f)
        dataset.setLineWidth(3f)

        val colorf = Color.rgb(101, 200, 237)
        dataset.setColor(colorf);
        dataset.setCircleColor(colorf)
        dataset.fillColor = colorf

        dataset.setHighLightColor(Color.BLUE)


        val formatter = object : IAxisValueFormatter {
            val decimalDigits: Int
                get() = 0

            override fun getFormattedValue(value: Float, axis: AxisBase): String {
                val sdf = SimpleDateFormat("MM/dd/yyyy")
                val netDate = Date((value * 1000).toLong())
                return sdf.format(netDate)

            }
        }

        lineChart.setTouchEnabled(false)

        lineChart.getXAxis().setValueFormatter(formatter)
        lineChart.getXAxis().isEnabled = false

        lineChart.getLayoutParams().height=600


        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setEnabled(false);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM)
        lineChart.getXAxis().setLabelCount(entries1.count(),true)

        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);


        lineChart.animateY(1000);


        val data = LineData(dataset)

        lineChart.setData(data)
        lineChart.invalidate() // refresh






    }
}
