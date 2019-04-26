package com.cynny.videoface.domain.model

import java.util.*
import kotlin.collections.HashMap

typealias VideoStats = SortedMap<Int, List<Stat>>

data class Stat(val counter: Int, val startTime: Int, val endTime: Int, val updateTime: Long, val source: String, val urlId: String,
                val ages: Ages, val genders: Genders, val emotions: Emotions)

typealias Emotions = HashMap<EmotionType, Double>

fun <T> HashMap<T, Double>.normalized(): HashMap<T, Double> {
    val sum = values.sum()
    return mapValues { it.value / sum } as HashMap
}

enum class EmotionType {
    ANGER, DISGUST, FEAR, SADNESS, SURPRISE, /*RESTED, */ HAPPINESS
}

typealias Ages = HashMap<Int, Double>

data class Genders(
        val female: Double = 0.0,
        val male: Double = 0.0
)

fun Genders.normalized(): Genders {
    val sum = female + male
    return Genders(female / sum, male / sum)
}