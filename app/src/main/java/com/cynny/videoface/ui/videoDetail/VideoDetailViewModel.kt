package com.cynny.videoface.ui.videoDetail

import androidx.lifecycle.ViewModel
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.*
import com.cynny.videoface.domain.usecase.GetVideoStatsUseCase
import com.cynny.videoface.ui.misc.livedata.ErrorLiveEvent
import com.cynny.videoface.ui.misc.livedata.ViewStateStore
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class VideoDetailViewModel @Inject constructor(private val getVideoStatsUseCase: GetVideoStatsUseCase) : ViewModel() {
    private val disposable = CompositeDisposable()

    val dataStore: ViewStateStore<VideoDetailViewState> = ViewStateStore(VideoDetailViewState())
    val emotionFilterStore: ViewStateStore<Pair<Array<EmotionType>, Array<Boolean>>> =
            ViewStateStore(Pair(EmotionType.values(), Array(EmotionType.values().size) { true }))
    val errorLiveEvent = ErrorLiveEvent()

    fun loadStats(id: String) {
        dataStore.dispatchState(dataStore.state().copy(loading = true))
        disposable.add(
                getVideoStatsUseCase.observable(GetVideoStatsUseCase.GetVideoStatsUseCaseParameters(id))
                        .subscribe { statsResource ->
                            Timber.d("stats = %s", statsResource)
                            when (statsResource) {
                                is Resource.Success -> {
                                    val samples = statsResource.data.mapValues { it ->
                                        val sumAges = Ages()
                                        var sumGenders = Genders()
                                        val sumEmotions = Emotions()
                                        var faces = 0
                                        it.value.asSequence().forEach { data ->
                                            val agesNormalized = data.ages.normalized()
                                            agesNormalized.forEach {
                                                sumAges[it.key] = (if (sumAges[it.key] != null) sumAges[it.key] else 0.0)!! + it.value
                                            }

                                            val gendersNormalized = data.genders.normalized()
                                            sumGenders = sumGenders.copy(male = gendersNormalized.male + sumGenders.male,
                                                    female = gendersNormalized.female + sumGenders.female)

                                            val emoNormalized = data.emotions.normalized()
                                            EmotionType.values().forEach { et ->
                                                sumEmotions[et] = (if (sumEmotions[et] != null) sumEmotions[et] else 0.0)!! + emoNormalized[et]!!
                                            }

                                            faces += data.counter
                                        }
                                        Sample(it.value[0].startTime, it.value[0].endTime, SampleData(sumAges.normalized(), sumGenders.normalized(), sumEmotions.normalized(), faces))
                                    }.toSortedMap()
                                    val globalAges = Ages()
                                    var globalGenders = Genders()
                                    val globalEmotions = Emotions()
                                    var globalFaces = 0
                                    samples.forEach {
                                        it.value.data.ages.forEach { a ->
                                            globalAges[a.key] = (if (globalAges[a.key] != null) globalAges[a.key] else 0.0)!! + a.value
                                        }

                                        globalGenders = globalGenders.copy(male = it.value.data.genders.male + globalGenders.male,
                                                female = it.value.data.genders.female + globalGenders.female)

                                        EmotionType.values().forEach { et ->
                                            globalEmotions[et] = (if (globalEmotions[et] != null) globalEmotions[et] else 0.0)!! +
                                                    it.value.data.emotions[et]!!
                                        }

                                        globalFaces = Math.max(it.value.data.faces, globalFaces)
                                    }
                                    dataStore.dispatchState(dataStore.state().copy(samples = samples,
                                            global = SampleData(globalAges.normalized(), globalGenders.normalized(), globalEmotions.normalized(), globalFaces),
                                            loading = false))
                                }
                                is Resource.Error -> {
                                    dataStore.dispatchState(dataStore.state().copy(loading = false))
                                    errorLiveEvent.value = statsResource.exception.message
                                }
                            }
                        })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun toggleEmotionFilter(emotionType: EmotionType) {
        val emotions = emotionFilterStore.state().first
        val filterStates = emotionFilterStore.state().second
        val checked = filterStates[emotions.indexOf(emotionType)]
        filterStates[emotions.indexOf(emotionType)] = !checked
        emotionFilterStore.dispatchState(Pair(emotions, filterStates))
    }
}
