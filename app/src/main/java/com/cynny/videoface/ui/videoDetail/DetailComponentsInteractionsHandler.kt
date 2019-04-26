package com.cynny.videoface.ui.videoDetail

import com.cynny.videoface.domain.model.EmotionType
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import java.util.*


class DetailComponentsInteractionsHandler(private val currentSection: CurrentSection,
                                          private val globalSection: GlobalSection,
                                          youtubeHandler: YoutubeHandler) {
    private var videoState: PlayerConstants.PlayerState? = null

    init {
        youtubeHandler.setYoutubePlayerListener(object : YoutubeHandler.YouTubePlayerListener {
            override fun onCurrentTime(time: Float) {
                onVideoTimeChange(time)
            }

            override fun onSeekEnd(second: Int) {
                onVideoTimeChange(second.toFloat())
            }

            override fun onStateChange(state: PlayerConstants.PlayerState) {
                videoState = state
            }
        })
    }

    private fun onVideoTimeChange(newTime: Float) {
        val newSecond = Math.floor(newTime.toDouble()).toInt()
        currentSection.updateSecond(newSecond)
    }

    fun onNewValues(samples: SortedMap<Int, Sample>, global: SampleData) {
        currentSection.updateSamples(samples)
        globalSection.update(global)
    }

    fun onEmotionFilterChange(emotionsFilter: Pair<Array<EmotionType>, Array<Boolean>>) {
        currentSection.updateEmotionVisibility(emotionsFilter)
    }
}