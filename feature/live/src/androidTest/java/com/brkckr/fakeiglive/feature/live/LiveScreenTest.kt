package com.brkckr.fakeiglive.feature.live

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class LiveScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun live_screen_renders_correctly() {
        val username = "test_user"
        composeTestRule.setContent {
            LiveScreen(
                state = LiveUiState(
                    profile = com.brkckr.fakeiglive.domain.model.UserProfile(
                        username = username,
                        profileImageUri = ""
                    )
                ),
                heartEvents = flowOf(),
                onIntent = {},
                onClose = {}
            )
        }

        // check if username is displayed
        composeTestRule.onNodeWithText(username).assertExists()
        
        // check if LIVE badge is visible
        val liveBadgeText = context.getString(R.string.live_badge)
        composeTestRule.onNodeWithText(liveBadgeText).assertExists()
    }

    @Test
    fun camera_off_fallback_is_visible_when_disabled() {
        composeTestRule.setContent {
            LiveScreen(
                state = LiveUiState(isCameraEnabled = false),
                heartEvents = flowOf(),
                onIntent = {},
                onClose = {}
            )
        }

        val cameraOffMsg = context.getString(R.string.live_camera_off)
        composeTestRule.onNodeWithText(cameraOffMsg).assertExists()
    }
}
