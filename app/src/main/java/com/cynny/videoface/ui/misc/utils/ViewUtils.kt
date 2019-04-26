package com.cynny.videoface.ui.misc.utils

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat


fun ProgressBar.tint(color: Int){
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val drawableProgress = DrawableCompat.wrap(indeterminateDrawable)
        DrawableCompat.setTint(drawableProgress, ContextCompat.getColor(context!!, color))
        indeterminateDrawable = DrawableCompat.unwrap<Drawable>(drawableProgress)
    } else {
        indeterminateDrawable.setColorFilter(ContextCompat.getColor(context!!, color), PorterDuff.Mode.SRC_IN)
    }
}