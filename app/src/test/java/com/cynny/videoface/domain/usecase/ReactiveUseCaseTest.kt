package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.shared.TestSchedulerRule
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Rule
import org.junit.Test


class ReactiveUseCaseTest {
    @Rule
    @JvmField
    val schedulerRule = TestSchedulerRule()

    class NormalUseCase : ReactiveUseCase<Unit, String>() {
        override fun action(parameters: Unit): Observable<Resource<String>> = Observable.just(Resource.Success(RESULT))

        companion object {
            const val RESULT = "RESULT"
        }
    }

    class ErrorUseCase : ReactiveUseCase<Unit, String>() {
        override fun action(parameters: Unit): Observable<Resource<String>> = Observable.just(Resource.Error(EXCEPTION))

        companion object {
            val EXCEPTION = RuntimeException()
        }
    }

    @Test
    fun `should return loading and success`() {
        val normalUseCase = NormalUseCase()
        val testObserver = TestObserver<Resource<String>>()
        normalUseCase.observable().subscribe(testObserver)

        normalUseCase.execute()

        schedulerRule.testScheduler.triggerActions()

        val v = testObserver.values()
        Assert.assertEquals(v.size, 2)
        Assert.assertEquals(v[0], Resource.Loading)
        Assert.assertEquals(v[1], Resource.Success(NormalUseCase.RESULT))
    }

    @Test
    fun `should return loading and failure`() {
        val errorUseCase = ErrorUseCase()
        val testObserver = TestObserver<Resource<String>>()
        errorUseCase.observable().subscribe(testObserver)

        errorUseCase.execute()

        schedulerRule.testScheduler.triggerActions()

        val v = testObserver.values()
        Assert.assertEquals(v.size, 2)
        Assert.assertEquals(v[0], Resource.Loading)
        Assert.assertEquals(v[1], Resource.Error(ErrorUseCase.EXCEPTION))
    }

    @Test
    fun `should return loading success two times on first, loading success one time on first`() {
        val normalUseCase = NormalUseCase()
        val testObserver1 = TestObserver<Resource<String>>()
        normalUseCase.observable().subscribe(testObserver1)

        normalUseCase.execute()

        schedulerRule.testScheduler.triggerActions()

        val testObserver2 = TestObserver<Resource<String>>()
        normalUseCase.observable().subscribe(testObserver2)

        normalUseCase.execute()

        schedulerRule.testScheduler.triggerActions()

        val v1 = testObserver1.values()
        Assert.assertEquals(v1.size, 4)
        Assert.assertEquals(v1[0], Resource.Loading)
        Assert.assertEquals(v1[1], Resource.Success(NormalUseCase.RESULT))
        Assert.assertEquals(v1[2], Resource.Loading)
        Assert.assertEquals(v1[3], Resource.Success(NormalUseCase.RESULT))

        val v2 = testObserver2.values()
        Assert.assertEquals(v2.size, 2)
        Assert.assertEquals(v2[0], Resource.Loading)
        Assert.assertEquals(v2[1], Resource.Success(NormalUseCase.RESULT))
    }

}