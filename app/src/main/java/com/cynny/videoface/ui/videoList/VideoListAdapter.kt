package com.cynny.videoface.ui.videoList

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cynny.videoface.R
import com.cynny.videoface.domain.model.Thumbnail
import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.model.VideoDetail
import com.cynny.videoface.ui.misc.utils.DurationFormatUtils
import com.cynny.videoface.ui.misc.utils.TimeAgo
import kotlinx.android.synthetic.main.video_list_item.view.*
import kotlinx.android.synthetic.main.video_list_item_with_data.view.*
import java.util.*


class VideoListAdapter(private val fragment: Fragment,
                       private val videoDetailRequestListener: VideoDetailRequestListener,
                       private val longClickListener: VideoLongClickListener,
                       private val clickListener: VideoClickListener,
                       var videos: List<Pair<Video, VideoDetail?>> = listOf()) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    private var selectionMode = false
    private var selectedItems = mutableListOf<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = videos[position].second
        if (detail != null) {
            bindDetail(holder, detail)
        } else {
            holder.switcher.displayedChild = 0
            videoDetailRequestListener.onDetailRequest(videos[position].first.id)
        }
        holder.setSelected(fragment.context!!, selectedItems.contains(videos[position].first))
        holder.onLongClick(longClickListener)
        holder.onClick(clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int,
                                  payloads: MutableList<Any>) {
        if (!payloads.isEmpty()) {
            val payload = payloads[0] as Bundle
            for (key in payload.keySet()) {
                if (key == Payload.KEY_DETAIL) {
                    videos[position].second?.let {
                        bindDetail(holder, it)
                    }
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
        holder.setSelected(fragment.context!!, selectedItems.contains(videos[position].first))
        holder.onLongClick(longClickListener)
        holder.onClick(clickListener)
    }

    private fun bindDetail(holder: ViewHolder, detail: VideoDetail) {
        holder.switcher.displayedChild = 1
        holder.title.text = detail.title
        holder.channel.text = detail.channel
        holder.publishedAt.text = String.format(fragment.getString(R.string.published), TimeAgo.toRelative(fragment.context!!, Date(detail.publishedAt), Date(), 1))
        holder.duration.text = DurationFormatUtils.fromMillis(detail.duration)

        var bestDiff = Float.MAX_VALUE
        var bestThumb: Thumbnail? = null
        val targetPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                fragment.resources.getDimension(R.dimen.list_item_thumb_width),
                fragment.resources.displayMetrics
        )
        detail.thumbnails.forEach {
            val d = targetPx - it.width
            if (d > 0 && d < bestDiff) {
                bestThumb = it
                bestDiff = d
            }
        }
        if (bestThumb == null && detail.thumbnails.isNotEmpty()) {
            bestThumb = detail.thumbnails[0]
        }
        if (bestThumb != null) {
            Glide.with(fragment)
                    .load(detail.thumbnails[0].url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.thumbnail)
        }
    }

    fun setValues(videos: List<Pair<Video, VideoDetail?>>) {
        val diffResult = DiffUtil.calculateDiff(VideoListAdapter.VideoListDiffCallback(this.videos, videos))
        this.videos = videos
        diffResult.dispatchUpdatesTo(this)
    }

    fun startSelectionMode() {
        selectionMode = true
    }

    fun select(position: Int) {
        videos.getOrNull(position)?.let {
            if (!selectedItems.contains(it.first)) {
                selectedItems.getOrNull(0)?.let {
                    selectedItems.remove(it)
                }
                selectedItems.add(it.first)
                notifyDataSetChanged()
            }
        }
    }

    fun selectedItems() = selectedItems

    fun stopSelectionMode() {
        selectionMode = false
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun isSelectionMode() = selectionMode

    class Payload {
        companion object {
            const val KEY_DETAIL = "KEY_DETAIL"
        }
    }

    class VideoListDiffCallback(private val old: List<Pair<Video, VideoDetail?>>,
                                private val new: List<Pair<Video, VideoDetail?>>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                old[oldItemPosition].first.id == new[newItemPosition].first.id

        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                (old[oldItemPosition].second == null && new[newItemPosition].second == null) ||
                        old[oldItemPosition].second?.equals(new[newItemPosition].second) ?: false

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]
            val diffBundle = Bundle()
            val oldDetail = oldItem.second
            val newDetail = newItem.second
            if (oldDetail == null && newDetail != null) {
                diffBundle.putBoolean(Payload.KEY_DETAIL, true)
            }
            return diffBundle
        }
    }

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        val switcher: ViewSwitcher = v.switcher
        val thumbnail: ImageView = v.thumbnail
        val title: TextView = v.title
        val channel: TextView = v.channel
        val publishedAt: TextView = v.publishedAt
        val duration: TextView = v.duration
        fun onLongClick(videoLongClickListener: VideoLongClickListener) {
            v.setOnLongClickListener {
                it?.run {
                    videoLongClickListener.onVideoLongClick(adapterPosition)
                }
                true
            }
        }

        fun onClick(videoClickListener: VideoClickListener) {
            v.setOnClickListener {
                it?.run {
                    videoClickListener.onVideoClick(adapterPosition)
                }
            }
        }

        fun setSelected(context: Context, selected: Boolean) {
            v.setBackgroundColor(if (selected)
                ContextCompat.getColor(context, R.color.secondaryColor)
            else
                ContextCompat.getColor(context, R.color.primaryColor))
        }
    }
}