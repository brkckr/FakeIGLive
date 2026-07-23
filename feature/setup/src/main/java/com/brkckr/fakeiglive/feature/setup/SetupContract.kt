package com.brkckr.fakeiglive.feature.setup

import com.brkckr.fakeiglive.core.util.UiText
import com.brkckr.fakeiglive.domain.model.UserProfile

// represent setup screen UI state
data class SetupUiState(
    val username: String = "", // user-defined display name
    val profileImageUri: String = "", // local uri of the selected avatar
    val usernameError: UiText? = null, // dynamic error message for username
    val showPhotoWarning: Boolean = false, // flag to highlight photo requirement
) {
    val isUsernameValid: Boolean
        get() = username.trim().length in USERNAME_LENGTH_RANGE // validation based on character count

    val isFormValid: Boolean
        get() = isUsernameValid && profileImageUri.isNotBlank() // check if all required fields are populated

    private companion object {
        val USERNAME_LENGTH_RANGE = 3..30
    }
}

sealed interface SetupIntent {
    data class UsernameChanged(val username: String) : SetupIntent

    data class ProfilePhotoSelected(val uri: String) : SetupIntent

    data object StartLiveClicked : SetupIntent
}

sealed interface SetupUiEffect {
    data class NavigateToLive(val profile: UserProfile) : SetupUiEffect
}
