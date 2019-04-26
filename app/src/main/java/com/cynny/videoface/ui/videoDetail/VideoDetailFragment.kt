package com.cynny.videoface.ui.videoDetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cynny.videoface.R
import com.cynny.videoface.ui.misc.ErrorListener
import com.cynny.videoface.ui.misc.ShareListener
import com.cynny.videoface.ui.misc.utils.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.video_detail_fragment.*
import javax.inject.Inject
import javax.inject.Provider
import com.cynny.videoface.ui.misc.utils.tint
import kotlinx.android.synthetic.main.video_detail_stats_section.view.*
import kotlinx.android.synthetic.main.video_detail_fragment.view.*


class VideoDetailFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModelProvider: Provider<VideoDetailViewModel>

    private val viewModel by lazy { viewModelFactory(this, viewModelProvider) { it.loadStats(videoId) } }

    private lateinit var videoId: String
    private lateinit var shareListener: ShareListener
    private lateinit var videoRemoteUrl: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.video_detail_fragment, container, false)
        v.flipper.progress.tint(R.color.primaryDarkColor)
        return v
    }

    private lateinit var detailInteractionsHandler: DetailComponentsInteractionsHandler


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        videoId = VideoDetailFragmentArgs.fromBundle(arguments).videoId
        val videoTitle = VideoDetailFragmentArgs.fromBundle(arguments).videoTitle
        videoRemoteUrl = VideoDetailFragmentArgs.fromBundle(arguments).videoRemoteUrl

        val youtubeHandler = YoutubeHandler(this, youtube_player_view)
        youtubeHandler.initialize(videoId, videoTitle)
        val globalSection = GlobalSection(stats_section.global_section)
        val currentSection = CurrentSection(stats_section.current_section)
        currentSection.emotionClickListener(viewModel::toggleEmotionFilter)

        detailInteractionsHandler = DetailComponentsInteractionsHandler(currentSection, globalSection, youtubeHandler)

        viewModel.dataStore.observe(viewLifecycleOwner) {
            if (!it.loading) {
                flipper.displayedChild = if (it.samples.isEmpty()) 1 else 2
                detailInteractionsHandler.onNewValues(it.samples, it.global)
            } else {
                flipper.displayedChild = 0
            }
        }
        viewModel.emotionFilterStore.observe(viewLifecycleOwner) {
            detailInteractionsHandler.onEmotionFilterChange(it)
        }
        viewModel.errorLiveEvent.observe(viewLifecycleOwner, object : ErrorListener {
            override fun onError(error: String) {
                flipper.displayedChild = 3
            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            shareListener = activity as ShareListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement ShareListener")
        }
    }

    fun onShareClicked() {
        shareListener.onShare(videoRemoteUrl)
    }
}