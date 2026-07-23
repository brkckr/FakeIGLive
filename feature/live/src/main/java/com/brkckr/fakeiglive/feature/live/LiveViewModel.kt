package com.brkckr.fakeiglive.feature.live

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brkckr.fakeiglive.core.util.UiText
import com.brkckr.fakeiglive.domain.model.DomainResult
import com.brkckr.fakeiglive.domain.model.UserProfile
import com.brkckr.fakeiglive.domain.usecase.ObserveCommentsUseCase
import com.brkckr.fakeiglive.domain.usecase.ObserveHeartsUseCase
import com.brkckr.fakeiglive.domain.usecase.ObserveViewerCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

// handle business logic, state updates, and side effects
@HiltViewModel
class LiveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, // persistence and navigation arguments
    observeComments: ObserveCommentsUseCase, // stream of mock comments
    observeViewerCount: ObserveViewerCountUseCase, // stream of viewer count updates
    observeHearts: ObserveHeartsUseCase, // stream of heart burst events
) : ViewModel() {
    private val username: String =
        requireNotNull(savedStateHandle["username"]) // extract username from navigation
    private val profileImageUri: String =
        requireNotNull(savedStateHandle["profileImageUri"]) // extract profile uri from navigation

    private val _uiState = MutableStateFlow(
        LiveUiState(
            profile = UserProfile(
                username = username,
                profileImageUri = profileImageUri,
            ),
        ),
    ) // initialize state with broadcaster info
    val uiState: StateFlow<LiveUiState> = _uiState.asStateFlow()

    private val _heartEvents = MutableSharedFlow<Long>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val heartEvents: SharedFlow<Long> = _heartEvents.asSharedFlow()

    private val _uiEffect = MutableSharedFlow<LiveUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val uiEffect: SharedFlow<LiveUiEffect> = _uiEffect.asSharedFlow()

    private val heartId = AtomicLong(0L)
    private val commentId = AtomicLong(0L)

    init {
        viewModelScope.launch {
            observeComments()
                .catch { _uiEffect.emit(LiveUiEffect.ShowError(UiText.StringResource(R.string.live_error_comments))) }
                .collect { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            _uiState.update { current ->
                                val incomingComment = LiveComment(
                                    id = commentId.incrementAndGet(),
                                    content = result.data,
                                )
                                current.copy(
                                    comments = (listOf(incomingComment) + current.comments)
                                        .take(MAX_COMMENTS)
                                        .toImmutableList(),
                                )
                            }
                        }

                        is DomainResult.Error -> {
                            _uiEffect.emit(LiveUiEffect.ShowError(UiText.StringResource(R.string.live_error_stream)))
                        }
                    }
                }
        }
        viewModelScope.launch {
            observeViewerCount()
                .catch { /* Keep last count */ }
                .collect { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            _uiState.update { it.copy(viewerCount = result.data) }
                        }

                        is DomainResult.Error -> {
                            _uiEffect.emit(LiveUiEffect.ShowError(UiText.StringResource(R.string.live_error_viewers)))
                        }
                    }
                }
        }
        viewModelScope.launch {
            observeHearts()
                .catch { /* Decorative, ignore */ }
                .collect { result ->
                    if (result is DomainResult.Success) {
                        emitHeart()
                    }
                }
        }
    }

    fun onIntent(intent: LiveIntent) {
        when (intent) {
            LiveIntent.ToggleCamera -> _uiState.update { it.copy(isCameraEnabled = !it.isCameraEnabled) }
            LiveIntent.SwitchCamera -> _uiState.update {
                it.copy(
                    cameraLensFacing = when (it.cameraLensFacing) {
                        CameraLensFacing.FRONT -> CameraLensFacing.BACK
                        CameraLensFacing.BACK -> CameraLensFacing.FRONT
                    },
                    isCameraEnabled = true,
                )
            }

            LiveIntent.ToggleMicrophone -> _uiState.update {
                it.copy(isMicrophoneMuted = !it.isMicrophoneMuted)
            }

            LiveIntent.ToggleEffects -> _uiState.update {
                it.copy(areEffectsEnabled = !it.areEffectsEnabled)
            }

            LiveIntent.SendHeart -> emitHeart()
        }
    }

    private fun emitHeart() {
        _heartEvents.tryEmit(heartId.incrementAndGet())
    }

    private companion object {
        // Keeps the oldest visible row close enough to animate out of the viewport.
        const val MAX_COMMENTS = 6
    }
}
