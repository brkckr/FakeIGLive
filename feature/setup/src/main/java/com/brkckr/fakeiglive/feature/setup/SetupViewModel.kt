package com.brkckr.fakeiglive.feature.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brkckr.fakeiglive.core.util.UiText
import com.brkckr.fakeiglive.domain.model.UserProfile
import com.brkckr.fakeiglive.domain.repository.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// manage setup process and form validation
@HiltViewModel
class SetupViewModel @Inject constructor(
    private val userPreferences: UserPreferences // dependency for local persistence
) : ViewModel() {
    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SetupUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val uiEffect: SharedFlow<SetupUiEffect> = _uiEffect.asSharedFlow()

    init {
        // load previously saved profile information on initialization
        viewModelScope.launch {
            userPreferences.getUserProfile().first()?.let { savedProfile ->
                _uiState.update {
                    it.copy(
                        username = savedProfile.username,
                        profileImageUri = savedProfile.profileImageUri
                    )
                }
            }
        }
    }

    fun onIntent(intent: SetupIntent) {
        when (intent) {
            is SetupIntent.UsernameChanged -> {
                val newUsername = intent.username.take(MAX_USERNAME_LENGTH)
                val error = if (newUsername.isNotBlank() && newUsername.trim().length < 3) {
                    UiText.StringResource(R.string.setup_error_username_too_short)
                } else null

                _uiState.update { it.copy(username = newUsername, usernameError = error) }
            }

            is SetupIntent.ProfilePhotoSelected -> {
                _uiState.update { it.copy(profileImageUri = intent.uri, showPhotoWarning = false) }
            }

            SetupIntent.StartLiveClicked -> startLive()
        }
    }

    private fun startLive() {
        val current = _uiState.value

        if (current.profileImageUri.isBlank()) {
            _uiState.update { it.copy(showPhotoWarning = true) }
        }

        if (!current.isFormValid) return

        viewModelScope.launch {
            // persist profile before navigation to remember user
            val profile = UserProfile(
                username = current.username.trim(),
                profileImageUri = current.profileImageUri,
            )
            userPreferences.saveUserProfile(profile)

            _uiEffect.emit(SetupUiEffect.NavigateToLive(profile))
        }
    }

    private companion object {
        const val MAX_USERNAME_LENGTH = 30 // maximum allowed characters for username
    }
}
