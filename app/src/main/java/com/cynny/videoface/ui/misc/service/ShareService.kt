package com.cynny.videoface.ui.misc.service

import android.app.Activity
import androidx.core.app.ShareCompat
import com.cynny.videoface.R
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class ShareService {
    fun share(url: String, activity: Activity): Single<Unit> = Single.timer(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setChooserTitle(activity.getString(R.string.share))
                        .setText(url)
                        .startChooser()
            }
}