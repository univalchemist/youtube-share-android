package com.cynny.videoface.ui.videoDetail

import android.view.View
import com.cynny.videoface.domain.model.Ages
import com.cynny.videoface.domain.model.EmotionType
import com.cynny.videoface.domain.model.Emotions
import com.cynny.videoface.domain.model.Genders
import kotlinx.android.synthetic.main.video_detail_stats_global_section.view.*
import java.text.DecimalFormat
import java.util.*


class GlobalSection(private val globalStat: View) {
    private val df = (DecimalFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).also { it.applyPattern("0.00") }

    private fun setEmotions(emotions: Emotions) {
        if (emotions.isNotEmpty()) {
            globalStat.percHappy.setPercentage((emotions[EmotionType.HAPPINESS]!! * 360).toInt())
            globalStat.percHappy.setStepCountText(df.format(emotions[EmotionType.HAPPINESS]))
            globalStat.percSurprise.setPercentage((emotions[EmotionType.SURPRISE]!! * 360).toInt())
            globalStat.percSurprise.setStepCountText(df.format(emotions[EmotionType.SURPRISE]))
            globalStat.percSad.setPercentage((emotions[EmotionType.SADNESS]!! * 360).toInt())
            globalStat.percSad.setStepCountText(df.format(emotions[EmotionType.SADNESS]))
            globalStat.percFear.setPercentage((emotions[EmotionType.FEAR]!! * 360).toInt())
            globalStat.percFear.setStepCountText(df.format(emotions[EmotionType.FEAR]))
            globalStat.percDisgust.setPercentage((emotions[EmotionType.DISGUST]!! * 360).toInt())
            globalStat.percDisgust.setStepCountText(df.format(emotions[EmotionType.DISGUST]))
            globalStat.percAngry.setPercentage((emotions[EmotionType.ANGER]!! * 360).toInt())
            globalStat.percAngry.setStepCountText(df.format(emotions[EmotionType.ANGER]))

            globalStat.percHappy.visibility = View.VISIBLE
            globalStat.percSurprise.visibility = View.VISIBLE
            globalStat.percSad.visibility = View.VISIBLE
            globalStat.percFear.visibility = View.VISIBLE
            globalStat.percDisgust.visibility = View.VISIBLE
            globalStat.percAngry.visibility = View.VISIBLE
        } else {
            globalStat.percHappy.visibility = View.GONE
            globalStat.percSurprise.visibility = View.GONE
            globalStat.percSad.visibility = View.GONE
            globalStat.percFear.visibility = View.GONE
            globalStat.percDisgust.visibility = View.GONE
            globalStat.percAngry.visibility = View.GONE
        }
    }

    private fun setAges(ages: Ages) {
        if (ages.isNotEmpty()) {
            var ageUnder18 = 0.0
            var age1835 = 0.0
            var age3551 = 0.0
            var ageOver51 = 0.0

            ages.forEach {
                when {
                    it.key <= 18 -> {
                        ageUnder18+=it.value
                    }
                    it.key in 19..35 -> {
                        age1835+=it.value
                    }
                    it.key in 36..51 -> {
                        age3551+=it.value
                    }
                    it.key > 51 -> {
                        ageOver51+=it.value
                    }
                }
            }
            globalStat.percAgeUnder18.setPercentage((ageUnder18 * 360).toInt())
            globalStat.percAgeUnder18.setStepCountText(df.format(ageUnder18))
            globalStat.percAge1835.setPercentage((age1835 * 360).toInt())
            globalStat.percAge1835.setStepCountText(df.format(age1835))
            globalStat.percAge3551.setPercentage((age3551 * 360).toInt())
            globalStat.percAge3551.setStepCountText(df.format(age3551))
            globalStat.percAgeOver51.setPercentage((ageOver51 * 360).toInt())
            globalStat.percAgeOver51.setStepCountText(df.format(ageOver51))

            globalStat.percAgeUnder18.visibility = View.VISIBLE
            globalStat.percAge1835.visibility = View.VISIBLE
            globalStat.percAge3551.visibility = View.VISIBLE
            globalStat.percAgeOver51.visibility = View.VISIBLE
        } else {
            globalStat.percAgeUnder18.visibility = View.GONE
            globalStat.percAge1835.visibility = View.GONE
            globalStat.percAge3551.visibility = View.GONE
            globalStat.percAgeOver51.visibility = View.GONE
        }
    }

    private fun setGenders(genders: Genders) {
        globalStat.percMale.setPercentage((genders.male * 360).toInt())
        globalStat.percMale.setStepCountText(df.format(genders.male))
        globalStat.percFemale.setPercentage((genders.female * 360).toInt())
        globalStat.percFemale.setStepCountText(df.format(genders.female))
        globalStat.percMale.visibility = View.VISIBLE
        globalStat.percFemale.visibility = View.VISIBLE
    }

    fun update(global: SampleData) {
        setEmotions(global.emotions)
        setAges(global.ages)
        setGenders(global.genders)
    }
}