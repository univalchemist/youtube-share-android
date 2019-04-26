package com.cynny.videoface.ui.videoDetail

import android.widget.ImageView
import android.widget.SeekBar
import com.cynny.videoface.R
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener


class YoutubeHandler(private val fragment: VideoDetailFragment, private val playerView: YouTubePlayerView) {
    private var player: YouTubePlayer? = null

    interface YouTubePlayerListener {
        fun onCurrentTime(time: Float)
        fun onStateChange(state: PlayerConstants.PlayerState)
        fun onSeekEnd(second: Int)
    }

    private var youtubePlayerListener: YouTubePlayerListener? = null
    fun setYoutubePlayerListener(youtubePlayerListener: YouTubePlayerListener) {
        this.youtubePlayerListener = youtubePlayerListener
    }

    fun initialize(videoId: String, videoTitle: String?) {
        fragment.lifecycle.addObserver(playerView)
        playerView.playerUIController.setVideoTitle(videoTitle ?: videoId)
        playerView.playerUIController.showYouTubeButton(false)
        playerView.playerUIController.showBackButton(true)
        playerView.playerUIController.setBackButtonClickListener { fragment.activity?.onBackPressed() }
        playerView.playerUIController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                if (seekbar != null) {
                    youtubePlayerListener?.onSeekEnd(seekbar.progress)
                }
            }
        })
        val imageView = ImageView(fragment.context)
        imageView.setImageResource(R.drawable.ic_share_white)
        playerView.playerUIController.addView(imageView)
        imageView.setOnClickListener {
            fragment.onShareClicked()
        }
        playerView.initialize({ initializedYouTubePlayer ->
            initializedYouTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    onInitializationSuccess(initializedYouTubePlayer, videoId)
                }
            })
        }, true)
    }

    private fun onInitializationSuccess(player: YouTubePlayer, videoId: String) {
        this.player = player
        player.loadVideo(videoId, 0F)
        player.addListener(object : AbstractYouTubePlayerListener() {
            override fun onCurrentSecond(time: Float) {
                youtubePlayerListener?.onCurrentTime(time)
            }

            override fun onStateChange(state: PlayerConstants.PlayerState) {
                youtubePlayerListener?.onStateChange(state)
            }
        })
    }

    fun pauseVideo() {
        player?.pause()
    }

    fun seekTo(time: Float) {
        // TODO replace this with setProgress of seekbar (to implement in lower library)
        player?.seekTo(time)
    }
}