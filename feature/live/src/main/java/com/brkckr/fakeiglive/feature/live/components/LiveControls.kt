package com.brkckr.fakeiglive.feature.live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.FaceRetouchingNatural
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.icons.outlined.MicOff
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.outlined.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.brkckr.fakeiglive.core.ui.theme.InstagramPink
import com.brkckr.fakeiglive.core.ui.theme.spacing
import com.brkckr.fakeiglive.feature.live.LiveIntent
import com.brkckr.fakeiglive.feature.live.LiveUiState
import com.brkckr.fakeiglive.feature.live.R

@Composable
fun LiveControlColumn(
    state: LiveUiState,
    onIntent: (LiveIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        LiveControlButton(
            icon = if (state.isMicrophoneMuted) Icons.Outlined.MicOff else Icons.Outlined.MicNone,
            contentDescription = stringResource(
                if (state.isMicrophoneMuted) R.string.live_unmute_microphone else R.string.live_mute_microphone,
            ),
            isActive = state.isMicrophoneMuted,
            onClick = { onIntent(LiveIntent.ToggleMicrophone) },
        )
        LiveControlButton(
            icon = if (state.isCameraEnabled) Icons.Outlined.Videocam else Icons.Outlined.VideocamOff,
            contentDescription = stringResource(
                if (state.isCameraEnabled) R.string.live_turn_camera_off else R.string.live_turn_camera_on,
            ),
            isActive = !state.isCameraEnabled,
            onClick = { onIntent(LiveIntent.ToggleCamera) },
        )
        LiveControlButton(
            icon = Icons.Outlined.Cameraswitch,
            contentDescription = stringResource(R.string.live_switch_camera),
            onClick = { onIntent(LiveIntent.SwitchCamera) },
        )
        LiveControlButton(
            icon = Icons.Outlined.FaceRetouchingNatural,
            contentDescription = stringResource(R.string.live_toggle_effects),
            isActive = state.areEffectsEnabled,
            onClick = { onIntent(LiveIntent.ToggleEffects) },
        )
    }
}

@Composable
private fun LiveControlButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    isActive: Boolean = false,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(MaterialTheme.spacing.massive - 12.dp) // 52dp
            .background(
                color = if (isActive) InstagramPink.copy(alpha = 0.82f) else Color.Transparent,
                shape = CircleShape,
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(MaterialTheme.spacing.extraLarge - 3.dp), // 29dp
            tint = Color.White,
        )
    }
}
