package com.brkckr.fakeiglive.feature.setup

import app.cash.turbine.test
import com.brkckr.fakeiglive.domain.repository.UserPreferences
import io.mockk.coEvery
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val userPreferences: UserPreferences = mockk()
    private lateinit var viewModel: SetupViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { userPreferences.getUserProfile() } returns flowOf(null)
        viewModel = SetupViewModel(userPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty and invalid`() = runTest {
        assertEquals("", viewModel.uiState.value.username)
        assertEquals("", viewModel.uiState.value.profileImageUri)
        assertFalse(viewModel.uiState.value.isFormValid)
    }

    @Test
    fun `username change updates state and length is capped`() = runTest {
        val longUsername = "a".repeat(40)
        viewModel.onIntent(SetupIntent.UsernameChanged(longUsername))
        
        assertEquals(30, viewModel.uiState.value.username.length)
        assertTrue(viewModel.uiState.value.isUsernameValid)
    }

    @Test
    fun `profile photo selected updates state`() = runTest {
        val uri = "content://media/photo"
        viewModel.onIntent(SetupIntent.ProfilePhotoSelected(uri))
        
        assertEquals(uri, viewModel.uiState.value.profileImageUri)
    }

    @Test
    fun `username too short sets error`() = runTest {
        viewModel.onIntent(SetupIntent.UsernameChanged("ab"))
        
        assertTrue(viewModel.uiState.value.usernameError != null)
        assertFalse(viewModel.uiState.value.isUsernameValid)
    }

    @Test
    fun `start live with empty photo sets warning`() = runTest {
        viewModel.onIntent(SetupIntent.UsernameChanged("brkckr"))
        viewModel.onIntent(SetupIntent.StartLiveClicked)
        
        assertTrue(viewModel.uiState.value.showPhotoWarning)
        assertFalse(viewModel.uiState.value.isFormValid)
    }

    @Test
    fun `valid form allows navigation to live`() = runTest {
        coEvery { userPreferences.saveUserProfile(any()) } returns Unit
        
        viewModel.uiEffect.test {
            viewModel.onIntent(SetupIntent.UsernameChanged("brkckr"))
            viewModel.onIntent(SetupIntent.ProfilePhotoSelected("uri"))
            viewModel.onIntent(SetupIntent.StartLiveClicked)

            val effect = awaitItem()
            assertTrue(effect is SetupUiEffect.NavigateToLive)
            assertEquals("brkckr", (effect as SetupUiEffect.NavigateToLive).profile.username)
        }
    }
}
