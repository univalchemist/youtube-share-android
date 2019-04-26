package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.repository.VideoRepository
import io.reactivex.Observable
import javax.inject.Inject

class DeleteVideoUseCase @Inject constructor(private val videoRepository: VideoRepository) : UseCase<DeleteVideoUseCase.DeleteVideoUseCaseParameters, Boolean>() {
    override fun action(parameters: DeleteVideoUseCaseParameters): Observable<Resource<Boolean>> = videoRepository.delete(parameters.video)

    data class DeleteVideoUseCaseParameters(val video: Video)
}