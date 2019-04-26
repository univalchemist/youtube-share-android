package com.cynny.videoface.data.source.remote.videoface.model

data class Data(
        val ages: Map<String, Int>,
        val emotions: Emotions,
        val genders: Genders
)