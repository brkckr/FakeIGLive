package com.brkckr.fakeiglive.data.repository

import com.brkckr.fakeiglive.data.datasource.LiveDataSource
import com.brkckr.fakeiglive.domain.model.Comment
import com.brkckr.fakeiglive.domain.model.DomainResult
import com.brkckr.fakeiglive.domain.repository.LiveRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

// manage stream emission and error handling logic
@Singleton
class LiveRepositoryImpl @Inject constructor(
    private val dataSource: LiveDataSource,
) : LiveRepository {
    private val random: Random = Random.Default

    override fun observeComments(): Flow<DomainResult<Comment>> = flow {
        val comments = dataSource.getComments()
        val delayRange = dataSource.getCommentDelayRange()
        var previousIndex = -1
        while (currentCoroutineContext().isActive) {
            var nextIndex = random.nextInt(comments.size)
            if (nextIndex == previousIndex) {
                nextIndex = (nextIndex + random.nextInt(1, comments.size)) % comments.size
            }
            emit(comments[nextIndex])
            previousIndex = nextIndex
            delay(random.nextLong(delayRange.first, delayRange.last + 1))
        }
    }.toDomainResult()

    override fun observeViewerCount(): Flow<DomainResult<Int>> = flow {
        val viewerRange = dataSource.getViewerRange()
        val changeRange = dataSource.getViewerChangeRange()
        val trendProbability = dataSource.getTrendChangeProbability()
        val delayRange = dataSource.getViewerDelayRange()

        var viewers = random.nextInt(viewerRange.first, viewerRange.last + 1)
        var direction = if (random.nextBoolean()) INCREASE else DECREASE
        emit(viewers)
        while (currentCoroutineContext().isActive) {
            delay(random.nextLong(delayRange.first, delayRange.last + 1))

            if (random.nextFloat() < trendProbability) {
                direction *= -1
            }

            val change = random.nextInt(changeRange.first, changeRange.last + 1)
            val candidate = viewers + (change * direction)
            if (candidate !in viewerRange) {
                direction *= -1
            }
            viewers = (viewers + change * direction).coerceIn(viewerRange.first, viewerRange.last)
            emit(viewers)
        }
    }.toDomainResult()

    override fun observeHearts(): Flow<DomainResult<Unit>> = flow {
        val delayRange = dataSource.getHeartDelayRange()
        while (currentCoroutineContext().isActive) {
            delay(random.nextLong(delayRange.first, delayRange.last + 1))
            emit(Unit)
        }
    }.toDomainResult()

    private fun <T> Flow<T>.toDomainResult(): Flow<DomainResult<T>> = this
        .map<T, DomainResult<T>> { DomainResult.Success(it) }
        .catch { emit(DomainResult.Error(it)) }

    private companion object {
        const val INCREASE = 1
        const val DECREASE = -1
    }
}
