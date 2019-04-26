package com.cynny.videoface.data.repository

import com.cynny.videoface.data.source.db.DbDataSource
import com.cynny.videoface.data.source.remote.youtube.YoutubeDataSource
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.Thumbnail
import com.cynny.videoface.domain.model.VideoDetail
import com.cynny.videoface.shared.TestSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException


class VideoRepositoryTest{
    @Rule
    @JvmField
    val schedulerRule = TestSchedulerRule()

    private val id = "ID"
    private val videoDetailLocalSuccessResource = Resource.Success(VideoDetail(id,"T", "DL", 1L, 2L, "Y",
            listOf(Thumbnail("U", 26, 9))))
    private val videoDetailEmptyResource = Resource.Success(null)
    private val videoDetailLocalErrorResource = Resource.Error(RuntimeException("L"))
    private val videoDetailRemoteErrorResource = Resource.Error(RuntimeException("R"))
    private val videoDetailRemoteSuccessResource = Resource.Success(VideoDetail(id,"TR", "DR", 1L, 2L, "Y",
            listOf(Thumbnail("U", 26, 9))))


    @Test
    fun `should return from local if local success`() {
        val dbDataSource = mockk<DbDataSource>(relaxed = true)
        every { dbDataSource.getVideoDetail(id) } returns Observable.just(videoDetailLocalSuccessResource)
        val youtubeDataSource = mockk<YoutubeDataSource>(relaxed = true)

        val testObserver = TestObserver<Resource<VideoDetail?>>()
        VideoRepository(dbDataSource, youtubeDataSource).getDetail(id).subscribe(testObserver)

        schedulerRule.testScheduler.triggerActions()

        val v = testObserver.values()
        Assert.assertEquals(v.size, 1)
        Assert.assertEquals(v[0], videoDetailLocalSuccessResource)
        verify(exactly = 1) { dbDataSource.getVideoDetail(id) }
        verify(exactly = 0) { youtubeDataSource.getVideoDetail(id) }
    }

    @Test
    fun `should call remote if local error`() {
        val dbDataSource = mockk<DbDataSource>(relaxed = true)
        every { dbDataSource.getVideoDetail(id) } returns Observable.just(videoDetailLocalErrorResource)
        val youtubeDataSource = mockk<YoutubeDataSource>(relaxed = true)

        val testObserver = TestObserver<Resource<VideoDetail?>>()
        VideoRepository(dbDataSource, youtubeDataSource).getDetail(id).subscribe(testObserver)

        schedulerRule.testScheduler.triggerActions()

        verify(exactly = 1) { dbDataSource.getVideoDetail(id) }
        verify(exactly = 1) { youtubeDataSource.getVideoDetail(id) }
    }

    @Test
    fun `should call remote if local empty`() {
        val dbDataSource = mockk<DbDataSource>(relaxed = true)
        every { dbDataSource.getVideoDetail(id) } returns Observable.just(videoDetailEmptyResource)
        val youtubeDataSource = mockk<YoutubeDataSource>(relaxed = true)

        val testObserver = TestObserver<Resource<VideoDetail?>>()
        VideoRepository(dbDataSource, youtubeDataSource).getDetail(id).subscribe(testObserver)

        schedulerRule.testScheduler.triggerActions()

        verify(exactly = 1) { dbDataSource.getVideoDetail(id) }
        verify(exactly = 1) { youtubeDataSource.getVideoDetail(id) }
    }

    @Test
    fun `should return remote error if local and remote error`() {
        val dbDataSource = mockk<DbDataSource>(relaxed = true)
        every { dbDataSource.getVideoDetail(id) } returns Observable.just(videoDetailLocalErrorResource)
        val youtubeDataSource = mockk<YoutubeDataSource>(relaxed = true)
        every { youtubeDataSource.getVideoDetail(id) } returns Single.just(videoDetailRemoteErrorResource)

        val testObserver = TestObserver<Resource<VideoDetail?>>()
        VideoRepository(dbDataSource, youtubeDataSource).getDetail(id).subscribe(testObserver)

        schedulerRule.testScheduler.triggerActions()

        verify(exactly = 1) { dbDataSource.getVideoDetail(id) }
        verify(exactly = 1) { youtubeDataSource.getVideoDetail(id) }
        val v = testObserver.values()
        Assert.assertEquals(v.size, 1)
        Assert.assertEquals(v[0], videoDetailRemoteErrorResource)
    }

    @Test
    fun `should return remote error if local and remote empty`() {
        val dbDataSource = mockk<DbDataSource>(relaxed = true)
        every { dbDataSource.getVideoDetail(id) } returns Observable.just(videoDetailEmptyResource)
        val youtubeDataSource = mockk<YoutubeDataSource>(relaxed = true)
        every { youtubeDataSource.getVideoDetail(id) } returns Single.just(videoDetailEmptyResource)

        val testObserver = TestObserver<Resource<VideoDetail?>>()
        VideoRepository(dbDataSource, youtubeDataSource).getDetail(id).subscribe(testObserver)

        schedulerRule.testScheduler.triggerActions()

        verify(exactly = 1) { dbDataSource.getVideoDetail(id) }
        verify(exactly = 1) { youtubeDataSource.getVideoDetail(id) }
        val v = testObserver.values()
        Assert.assertEquals(v.size, 1)
        Assert.assertEquals(v[0], videoDetailEmptyResource)
    }

    @Test
    fun `should call db insert if local error and remote success`() {
        val dbDataSource = mockk<DbDataSource>(relaxed = true)
        every { dbDataSource.getVideoDetail(id) } returns Observable.just(videoDetailLocalErrorResource)
        every { dbDataSource.addVideoDetail(videoDetailRemoteSuccessResource.data) } returns Completable.complete()
        val youtubeDataSource = mockk<YoutubeDataSource>(relaxed = true)
        every { youtubeDataSource.getVideoDetail(id) } returns Single.just(videoDetailRemoteSuccessResource)

        val testObserver = TestObserver<Resource<VideoDetail?>>()
        VideoRepository(dbDataSource, youtubeDataSource).getDetail(id).subscribe(testObserver)

        schedulerRule.testScheduler.triggerActions()

        verify(exactly = 1) { dbDataSource.getVideoDetail(id) }
        verify(exactly = 1) { youtubeDataSource.getVideoDetail(id) }
        verify(exactly = 1) { dbDataSource.addVideoDetail(videoDetailRemoteSuccessResource.data) }
    }
}