package com.cynny.videoface.ui.videoDetail

import android.view.View
import com.cynny.videoface.R
import com.cynny.videoface.domain.model.EmotionType
import kotlinx.android.synthetic.main.video_detail_stats_current_section.view.*
import java.text.DecimalFormat
import java.util.*


class CurrentSection(private val currentSection: View, private var samples: SortedMap<Int, Sample> = TreeMap<Int, Sample>()) {
    private val df = DecimalFormat("#.##")
    private var placeholderEmo: String = currentSection.context.getString(R.string.emo_placeholder)
    private var placeholderFaces: String = currentSection.context.getString(R.string.faces_placeholder)

    private var statisticsChartHandler: StatisticsChartHandler = StatisticsChartHandler(currentSection.context, currentSection.chart)

    private fun setCurrent(sample: Sample?) {
        sample?.let {
            currentSection.angerText.text = df.format(it.data.emotions[EmotionType.ANGER])
            currentSection.disgustText.text = df.format(it.data.emotions[EmotionType.DISGUST])
            currentSection.fearText.text = df.format(it.data.emotions[EmotionType.FEAR])
            currentSection.sadText.text = df.format(it.data.emotions[EmotionType.SADNESS])
            currentSection.surpriseText.text = df.format(it.data.emotions[EmotionType.SURPRISE])
            /*currentSection.restedText.text = df.format(it.data.emotions[EmotionType.RESTED])*/
            currentSection.happyText.text = df.format(it.data.emotions[EmotionType.HAPPINESS])
            currentSection.faces.text = it.data.faces.toString()
        } ?: run {
            currentSection.angerText.text = placeholderEmo
            currentSection.disgustText.text = placeholderEmo
            currentSection.fearText.text = placeholderEmo
            currentSection.sadText.text = placeholderEmo
            currentSection.surpriseText.text = placeholderEmo
            /*currentSection.restedText.text = placeholderEmo*/
            currentSection.happyText.text = placeholderEmo
            currentSection.faces.text = placeholderFaces
        }
    }

    fun emotionClickListener(listener: (EmotionType) -> Unit) {
        currentSection.angerImage.setOnClickListener { listener(EmotionType.ANGER) }
        currentSection.disgustImage.setOnClickListener { listener(EmotionType.DISGUST) }
        currentSection.fearImage.setOnClickListener { listener(EmotionType.FEAR) }
        currentSection.sadImage.setOnClickListener { listener(EmotionType.SADNESS) }
        currentSection.surpriseImage.setOnClickListener { listener(EmotionType.SURPRISE) }
        /*currentSection.restedImage.setOnClickListener { listener(EmotionType.RESTED) }*/
        currentSection.happyImage.setOnClickListener { listener(EmotionType.HAPPINESS) }
    }

    fun updateEmotionVisibility(emotionsFilter: Pair<Array<EmotionType>, Array<Boolean>>) {
        emotionsFilter.first.forEachIndexed { index, emotionType ->
            when (emotionType) {
                EmotionType.ANGER -> listOf(currentSection.angerImage, currentSection.angerText, currentSection.angerLabel)
                EmotionType.DISGUST -> listOf(currentSection.disgustImage, currentSection.disgustText, currentSection.disgustLabel)
                EmotionType.FEAR -> listOf(currentSection.fearImage, currentSection.fearText, currentSection.fearLabel)
                EmotionType.SADNESS -> listOf(currentSection.sadImage, currentSection.sadText, currentSection.sadLabel)
                EmotionType.SURPRISE -> listOf(currentSection.surpriseImage, currentSection.surpriseText, currentSection.surpriseLabel)
                /*EmotionType.RESTED -> listOf(currentSection.restedImage, currentSection.restedText, currentSection.restedLabel)*/
                EmotionType.HAPPINESS -> listOf(currentSection.happyImage, currentSection.happyText, currentSection.happyLabel)
            }.forEach {
                it.alpha = if (emotionsFilter.second[index]) {
                    1.0F
                } else {
                    0.2F
                }
            }
        }

        statisticsChartHandler.updateFilter(emotionsFilter.first.filterIndexed { index, _ -> emotionsFilter.second[index] })
    }

    fun updateSecond(newSecond: Int) {
        setCurrent(samples[newSecond])
        statisticsChartHandler.moveTo(newSecond)
    }

    fun updateSamples(samples: SortedMap<Int, Sample>) {
        this.samples = samples
        statisticsChartHandler.show(samples)
    }
}