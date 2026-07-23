package com.brkckr.fakeiglive.feature.setup

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class SetupScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun initial_state_shows_correct_elements() {
        composeTestRule.setContent {
            SetupScreen(
                state = SetupUiState(),
                onIntent = {}
            )
        }

        val title = context.getString(R.string.setup_title)
        val buttonText = context.getString(R.string.setup_start_live)

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(buttonText).assertExists()
    }

    @Test
    fun invalid_form_disables_start_button() {
        composeTestRule.setContent {
            SetupScreen(
                state = SetupUiState(username = "ab"), // too short
                onIntent = {}
            )
        }

        val buttonText = context.getString(R.string.setup_start_live)
        composeTestRule.onNodeWithText(buttonText).assertIsNotEnabled()
    }

    @Test
    fun valid_form_enables_start_button() {
        composeTestRule.setContent {
            SetupScreen(
                state = SetupUiState(
                    username = "brkckr",
                    profileImageUri = "content://something"
                ),
                onIntent = {}
            )
        }

        val buttonText = context.getString(R.string.setup_start_live)
        composeTestRule.onNodeWithText(buttonText).assertIsEnabled()
    }
}
