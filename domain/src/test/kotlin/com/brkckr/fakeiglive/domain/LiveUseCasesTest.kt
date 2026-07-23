package com.brkckr.fakeiglive.domain

import app.cash.turbine.test
import com.brkckr.fakeiglive.domain.model.Comment
import com.brkckr.fakeiglive.domain.model.DomainResult
import com.brkckr.fakeiglive.domain.repository.LiveRepository
import com.brkckr.fakeiglive.domain.usecase.ObserveCommentsUseCase
import com.brkckr.fakeiglive.domain.usecase.ObserveViewerCountUseCase
import com.brkckr.fakeiglive.domain.usecase.ObserveHeartsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LiveUseCasesTest {
    private val repository: LiveRepository = mockk()

    @Test
    fun `comments use case delegates to repository`() = runTest {
        val expected = Comment("viewer", "hello", "avatar")
        every { repository.observeComments() } returns flowOf(DomainResult.Success(expected))

        val useCase = ObserveCommentsUseCase(repository)
        useCase().test {
            val result = awaitItem()
            assertEquals(DomainResult.Success(expected), result)
            awaitComplete()
        }
    }

    @Test
    fun `viewer use case delegates to repository`() = runTest {
        val expected = 999
        every { repository.observeViewerCount() } returns flowOf(DomainResult.Success(expected))

        val useCase = ObserveViewerCountUseCase(repository)
        useCase().test {
            val result = awaitItem()
            assertEquals(DomainResult.Success(expected), result)
            awaitComplete()
        }
    }

    @Test
    fun `heart use case delegates to repository`() = runTest {
        every { repository.observeHearts() } returns flowOf(DomainResult.Success(Unit))

        val useCase = ObserveHeartsUseCase(repository)
        useCase().test {
            val result = awaitItem()
            assertEquals(DomainResult.Success(Unit), result)
            awaitComplete()
        }
    }
}
