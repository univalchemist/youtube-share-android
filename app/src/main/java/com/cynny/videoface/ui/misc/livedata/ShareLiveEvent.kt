package com.cynny.videoface.ui.misc.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.cynny.videoface.ui.misc.ShareListener


class ShareLiveEvent : SingleLiveEvent<String>() {
    fun observe(owner: LifecycleOwner, listener: ShareListener) {
        super.observe(owner, Observer { t ->
            listener.onShare(t)
        })
    }
}
