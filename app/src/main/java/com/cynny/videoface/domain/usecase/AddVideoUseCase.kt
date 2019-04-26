package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.exception.BadUrlException
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.repository.VideoRepository
import com.cynny.videoface.domain.service.IdExtractor
import com.cynny.videoface.domain.service.RemoteUrlCreator
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class AddVideoUseCase @Inject constructor(private val videoRepository: VideoRepository, private val idExtractor: IdExtractor,
                                          private val remoteUrlCreator: RemoteUrlCreator) : UseCase<AddVideoUseCase.AddVideoUseCaseParameters, Video>() {
    override fun action(parameters: AddVideoUseCaseParameters): Observable<Resource<Video>> {
        val id = idExtractor.extractId(parameters.srcUrl)
        return if (id != null) {
            videoRepository.add(Video(id, parameters.srcUrl, remoteUrlCreator.getUrl(id), Date().time))
        } else {
            Single.just(Resource.Error(BadUrlException()) as Resource<Video>).toObservable()
        }
    }

    data class AddVideoUseCaseParameters(val srcUrl: String)
}