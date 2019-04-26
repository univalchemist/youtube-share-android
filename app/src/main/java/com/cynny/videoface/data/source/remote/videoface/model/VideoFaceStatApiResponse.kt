package com.cynny.videoface.data.source.remote.videoface.model

import com.cynny.videoface.domain.model.EmotionType
import com.cynny.videoface.domain.model.Stat

data class VideoFaceStatApiResponse(
        val counter: Int,
        val v_time: Int,
        val u_time: Long,
        val source: String,
        val url_id: String,
        val ages: Map<String, Int>,
        val genders: Genders,
        val emotions: Emotions
) {
    companion object {
        fun toDomain(obj: VideoFaceStatApiResponse): Stat {
            val emotions = com.cynny.videoface.domain.model.Emotions()
            emotions[EmotionType.ANGER] = obj.emotions.anger
            emotions[EmotionType.DISGUST] = obj.emotions.disgust
            emotions[EmotionType.FEAR] = obj.emotions.fear
            emotions[EmotionType.SADNESS] = obj.emotions.sadness
            emotions[EmotionType.SURPRISE] = obj.emotions.surprise
            /*emotions[EmotionType.RESTED] = obj.emotions.rested*/
            emotions[EmotionType.HAPPINESS] = obj.emotions.happiness
            return Stat(obj.counter, obj.v_time, obj.v_time + 1, obj.u_time, obj.source, obj.url_id,
                    com.cynny.videoface.domain.model.Ages(obj.ages.mapKeys { it.key.toInt() }.mapValues { it.value.toDouble() }),
                    com.cynny.videoface.domain.model.Genders(female = obj.genders.female.toDouble(), male = obj.genders.male.toDouble()),
                    emotions
            )
        }
    }
}