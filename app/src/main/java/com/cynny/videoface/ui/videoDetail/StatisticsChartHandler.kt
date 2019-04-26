package com.cynny.videoface.ui.videoDetail

import android.content.Context
import androidx.core.content.ContextCompat
import com.cynny.videoface.R
import com.cynny.videoface.domain.model.EmotionType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.*
import com.github.mikephil.charting.components.LimitLine



class StatisticsChartHandler(private val context: Context, private val chart: LineChart) {
    private var datasets = mapOf<EmotionType, MutableList<LineDataSet>>()
    private var emotionsFilter = EmotionType.values().toList()

    companion object {
        private const val MAX_X_VISIBLE = 12F
    }

    init {
        chart.setTouchEnabled(false)
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.extraBottomOffset = 8.0F

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setDrawAxisLine(false)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.granularity = 5.0f
        chart.xAxis.yOffset = 8.0F

        chart.axisRight.isEnabled = false

        chart.isScaleYEnabled = false
        chart.axisLeft.labelCount = 5
        chart.axisLeft.xOffset = 8.0F
        chart.axisLeft.axisMinimum = -0.01F
        chart.axisLeft.axisMaximum = 1.01F
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisLeft.setDrawTopYLabelEntry(true)
        chart.axisLeft.enableGridDashedLine(5.0f, 5.0f, 2.0f)
    }

    fun show(samples: SortedMap<Int, Sample>) {
        val datasetsSamples = mutableMapOf<EmotionType, MutableList<Double>>()
        val x = ArrayList<Int>()
        samples.forEach { (i, sample) ->
            EmotionType.values().forEach {
                if (datasetsSamples[it] == null) {
                    datasetsSamples[it] = mutableListOf()
                }
                datasetsSamples[it]!!.add(sample.data.emotions[it]!!)
            }
            x.add(i)
        }

        datasets = datasetsSamples.mapValues {
            val datasetList = mutableListOf<LineDataSet>()
            createDataSet(datasetList, x, it.value, x.indices, it.key.color(context))
            datasetList
        }
        filterAndShow()
    }

    private fun filterAndShow() {
        val data = LineData()
        datasets.forEach {
            if (it.key in emotionsFilter) {
                addDatasets(data, it.value)
            }
        }
        chart.data = data
        chart.setVisibleXRange(0.0F, MAX_X_VISIBLE)
        chart.invalidate()
    }

    private fun addDatasets(data: LineData, datasets: List<LineDataSet>) {
        datasets.forEach { data.addDataSet(it) }
    }

    private fun createDataSet(datasets: MutableList<LineDataSet>, x: List<Int>, y: List<Double>, indexes: IntRange, color: Int) {
        val entries = ArrayList<Entry>()
        var isFirst = true
        var last = Int.MAX_VALUE
        for (i in indexes) {
            if (isFirst || x[i] - last == 1) {
                isFirst = false
                last = x[i]
                entries.add(Entry(x[i].toFloat(), y[i].toFloat()))
            } else {
                createDataSet(datasets, x, y, i..indexes.last, color)
                break
            }
        }

        if (entries.isNotEmpty()) {
            val dataset = LineDataSet(entries, "")
            dataset.lineWidth = 3.0F
            dataset.circleRadius = 4.0F
            dataset.color = color
            dataset.circleHoleRadius = 1.5F
            dataset.cubicIntensity = 0.05F
            dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
            dataset.setDrawCircles(false)
            dataset.setDrawValues(false)
            datasets.add(dataset)
        }
    }

    fun updateFilter(emotionsFilter: List<EmotionType>) {
        this.emotionsFilter = emotionsFilter
        filterAndShow()
    }

    fun moveTo(newSecond: Int) {
        chart.moveViewToX(newSecond - MAX_X_VISIBLE/2)
        chart.xAxis.removeAllLimitLines()
        val llXAxis = LimitLine(newSecond.toFloat()).apply {
            lineWidth = 2f
            lineColor = ContextCompat.getColor(context, R.color.secondaryTextColor)
            enableDashedLine(10f, 10f, 0f)
        }
        chart.xAxis.addLimitLine(llXAxis)
    }
}
