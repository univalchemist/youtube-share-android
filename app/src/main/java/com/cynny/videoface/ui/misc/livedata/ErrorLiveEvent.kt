package com.cynny.videoface.ui.misc.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.cynny.videoface.ui.misc.ErrorListener


class ErrorLiveEvent : SingleLiveEvent<String>() {
    fun observe(owner: LifecycleOwner, listener: ErrorListener) {
        super.observe(owner, Observer { t ->
            listener.onError(t)
        })
    }
}
