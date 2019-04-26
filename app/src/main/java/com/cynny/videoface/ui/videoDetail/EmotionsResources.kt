package com.cynny.videoface.ui.videoDetail

import android.content.Context
import androidx.core.content.ContextCompat
import com.cynny.videoface.R
import com.cynny.videoface.domain.model.EmotionType


fun EmotionType.color(context: Context) = ContextCompat.getColor(context, when (this) {
    EmotionType.ANGER -> R.color.anger
    EmotionType.DISGUST -> R.color.disgust
    EmotionType.FEAR -> R.color.fear
    EmotionType.SADNESS -> R.color.sadness
    EmotionType.SURPRISE -> R.color.surprise
    /*EmotionType.RESTED -> R.color.rested*/
    EmotionType.HAPPINESS -> R.color.happiness
}
)