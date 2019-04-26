package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.model.VideoStats
import com.cynny.videoface.domain.repository.StatsRepository
import javax.inject.Inject

class GetVideoStatsUseCase @Inject constructor(private val statRepository: StatsRepository) : UseCase<GetVideoStatsUseCase.GetVideoStatsUseCaseParameters, VideoStats>() {
    override fun action(parameters: GetVideoStatsUseCaseParameters) = statRepository.get(parameters.id)

    data class GetVideoStatsUseCaseParameters(val id: String)
}