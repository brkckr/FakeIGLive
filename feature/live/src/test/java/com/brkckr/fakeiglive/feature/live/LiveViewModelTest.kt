package com.brkckr.fakeiglive.feature.live

import androidx.lifecycle.SavedStateHandle
import com.brkckr.fakeiglive.domain.model.Comment
import com.brkckr.fakeiglive.domain.model.DomainResult
import com.brkckr.fakeiglive.domain.usecase.ObserveCommentsUseCase
import com.brkckr.fakeiglive.domain.usecase.ObserveViewerCountUseCase
import com.brkckr.fakeiglive.domain.usecase.ObserveHeartsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LiveViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val observeComments: ObserveCommentsUseCase = mockk()
    private val observeViewerCount: ObserveViewerCountUseCase = mockk()
    private val observeHearts: ObserveHeartsUseCase = mockk()
    private lateinit var viewModel: LiveViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "username" to "brkckr",
                "profileImageUri" to "uri"
            )
        )

        every { observeComments() } returns flowOf()
        every { observeViewerCount() } returns flowOf()
        every { observeHearts() } returns flowOf()

        viewModel = LiveViewModel(
            savedStateHandle = savedStateHandle,
            observeComments = observeComments,
            observeViewerCount = observeViewerCount,
            observeHearts = observeHearts
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correctly set from savedStateHandle`() = runTest {
        assertEquals("brkckr", viewModel.uiState.value.profile.username)
        assertEquals("uri", viewModel.uiState.value.profile.profileImageUri)
    }

    @Test
    fun `new comments are added to state`() = runTest {
        val comment = Comment("user", "test", "img")
        every { observeComments() } returns flowOf(DomainResult.Success(comment))

        val viewModel = LiveViewModel(
            savedStateHandle = SavedStateHandle(mapOf("username" to "b", "profileImageUri" to "u")),
            observeComments = observeComments,
            observeViewerCount = observeViewerCount,
            observeHearts = observeHearts
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.comments.size)
        assertEquals("test", viewModel.uiState.value.comments[0].content.text)
    }

    @Test
    fun `viewer count updates correctly`() = runTest {
        every { observeViewerCount() } returns flowOf(DomainResult.Success(5000))

        val viewModel = LiveViewModel(
            savedStateHandle = SavedStateHandle(mapOf("username" to "b", "profileImageUri" to "u")),
            observeComments = observeComments,
            observeViewerCount = observeViewerCount,
            observeHearts = observeHearts
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(5000, viewModel.uiState.value.viewerCount)
    }

    @Test
    fun `toggle camera intent updates state`() = runTest {
        val initialState = viewModel.uiState.value.isCameraEnabled
        viewModel.onIntent(LiveIntent.ToggleCamera)
        assertEquals(!initialState, viewModel.uiState.value.isCameraEnabled)
    }
}
