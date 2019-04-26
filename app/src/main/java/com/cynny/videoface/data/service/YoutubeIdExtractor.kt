package com.cynny.videoface.data.service

import com.cynny.videoface.domain.service.IdExtractor
import java.util.regex.Pattern


class YoutubeIdExtractor : IdExtractor {
    override fun extractId(url: String): String? {
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(url)
        return if (matcher.find()) {
            matcher.group()
        } else{
            null
        }
    }
}