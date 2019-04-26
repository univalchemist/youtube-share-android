package com.cynny.videoface.ui.videoList

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import com.cynny.videoface.R
import com.cynny.videoface.ui.misc.ErrorListener
import com.cynny.videoface.ui.misc.ShareListener
import com.cynny.videoface.ui.misc.utils.ViewModelFactory
import com.cynny.videoface.ui.misc.utils.tint
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.video_list_fragment.*
import kotlinx.android.synthetic.main.video_list_fragment.view.*
import javax.inject.Inject
import javax.inject.Provider


class VideoListFragment : DaggerFragment(), VideoLongClickListener, VideoClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var viewModelProvider: Provider<VideoListViewModel>
    private val viewModel by lazy { viewModelFactory(this, viewModelProvider) }
    private lateinit var adapter: VideoListAdapter
    private lateinit var shareListener: ShareListener
    private var showInfoMenuItem = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.list_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.info)?.isVisible = showInfoMenuItem
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.info -> {
                val popup = PopupWindow(context)
                val l = layoutInflater?.inflate(R.layout.info_popup, null, false)
                popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popup.elevation = 10.0F
                }
                popup.contentView = l
                popup.isOutsideTouchable = true
                popup.isFocusable = true
                l?.findViewById<View>(R.id.popup_open_youtube)?.setOnClickListener {
                    startYoutube()
                    popup.dismiss()
                }
                popup.showAsDropDown(activity?.findViewById(R.id.info))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.video_list_fragment, container, false)
        v.viewFlipper.progress.tint(R.color.primaryDarkColor)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appBar.toolbar.navigationIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_navigation)

        adapter = VideoListAdapter(this, viewModel, this, this)
        videoList.adapter = adapter
        videoList.layoutManager = LinearLayoutManager(activity)

        open_youtube.setOnClickListener {
            startYoutube()
        }

        viewModel.store.observe(viewLifecycleOwner) {
            if (!it.loading) {
                viewFlipper.displayedChild = if (it.videos.isEmpty()) 1 else 2
                showInfoMenuItem = it.videos.isNotEmpty()
                activity?.invalidateOptionsMenu()
                adapter.setValues(it.videos.toList())
            } else {
                viewFlipper.displayedChild = 0
            }
        }

        viewModel.errorLiveEvent.observe(viewLifecycleOwner, object : ErrorListener {
            override fun onError(error: String) {}
        })
    }

    private fun startYoutube() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com")))
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.title = getString(R.string.video_list_title)
        }
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            shareListener = activity as ShareListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement ShareListener")
        }

    }

    override fun onVideoLongClick(position: Int): Boolean {
        toolbar.startActionMode(object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.share ->
                        adapter.selectedItems().getOrNull(0)?.let {
                            shareListener.onShare(it.remoteUrl)
                        }
                    R.id.delete ->
                        adapter.selectedItems().getOrNull(0)?.let {
                            viewModel.onDelete(it)
                        }
                }

                mode?.finish()
                return true
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                adapter.startSelectionMode()
                adapter.select(position)
                val share = menu?.add(0, R.id.share, Menu.NONE, "Share")
                share?.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_share)
                val delete = menu?.add(0, R.id.delete, Menu.NONE, "Delete")
                delete?.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_trash)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                adapter.stopSelectionMode()
            }
        })
        return true
    }

    override fun onVideoClick(position: Int): Boolean {
        if (adapter.isSelectionMode()) {
            adapter.select(position)
        } else {
            val video = adapter.videos[position]
            findNavController().navigate(VideoListFragmentDirections.ActionVideoListFragmentToVideoDetailFragment(video.first.id, video.first.remoteUrl, video.second?.title
                    ?: ""))
        }
        return true
    }
}
