package com.cynny.videoface.ui.videoDetail

import com.cynny.videoface.domain.model.Ages
import com.cynny.videoface.domain.model.Emotions
import com.cynny.videoface.domain.model.Genders
import java.util.*


data class VideoDetailViewState(val samples: SortedMap<Int, Sample> = TreeMap<Int, Sample>(),
                                val global: SampleData = SampleData(),
                                val loading: Boolean = false)

data class Sample(val startTime: Int, val endTime: Int, val data: SampleData)

data class SampleData(val ages: Ages = Ages(), val genders: Genders = Genders(), val emotions: Emotions = Emotions(), val faces: Int = 0)