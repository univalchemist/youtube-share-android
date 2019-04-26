package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.repository.VideoRepository
import javax.inject.Inject


class GetVideosUseCase @Inject constructor(private val videoRepository: VideoRepository) : ReactiveUseCase<Unit, List<Video>>() {
    override fun action(parameters: Unit) = videoRepository.getAll()
}