package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.model.VideoDetail
import com.cynny.videoface.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideoDetailUseCase @Inject constructor(private val videoRepository: VideoRepository) : UseCase<GetVideoDetailUseCase.GetVideoDetailUseCaseParameters, VideoDetail?>() {
    override fun action(parameters: GetVideoDetailUseCaseParameters) = videoRepository.getDetail(parameters.id)

    data class GetVideoDetailUseCaseParameters(val id: String)
}