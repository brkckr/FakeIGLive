package com.brkckr.fakeiglive.feature.live

import com.brkckr.fakeiglive.core.util.UiText
import com.brkckr.fakeiglive.domain.model.Comment
import com.brkckr.fakeiglive.domain.model.UserProfile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class CameraLensFacing {
    FRONT,
    BACK,
}

data class LiveComment(
    val id: Long,
    val content: Comment,
)

// define immutable state and interaction models
data class LiveUiState(
    val profile: UserProfile = UserProfile(
        username = "",
        profileImageUri = ""
    ), // broadcaster profile info
    val comments: ImmutableList<LiveComment> = persistentListOf(), // thread-safe list of incoming comments
    val viewerCount: Int = 10_000, // simulated active viewer count
    val cameraLensFacing: CameraLensFacing = CameraLensFacing.FRONT, // current camera sensor in use
    val isCameraEnabled: Boolean = true, // master switch for camera preview
    val isMicrophoneMuted: Boolean = false, // status of audio input
    val areEffectsEnabled: Boolean = false, // toggle for visual filters
)

sealed interface LiveIntent {
    data object ToggleCamera : LiveIntent

    data object SwitchCamera : LiveIntent

    data object ToggleMicrophone : LiveIntent

    data object ToggleEffects : LiveIntent

    data object SendHeart : LiveIntent
}

sealed interface LiveUiEffect {
    data class ShowError(val message: UiText) : LiveUiEffect // display error via uitext wrapper
}
