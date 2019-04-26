package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideoUseCase @Inject constructor(private val videoRepository: VideoRepository) : UseCase<GetVideoUseCase.GetVideoUseCaseParameters, Video?>() {
    override fun action(parameters: GetVideoUseCaseParameters) = videoRepository.get(parameters.id)

    data class GetVideoUseCaseParameters(val id: String)
}