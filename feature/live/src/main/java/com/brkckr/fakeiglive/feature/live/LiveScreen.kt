package com.brkckr.fakeiglive.feature.live

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brkckr.fakeiglive.core.extensions.SystemBarsAppearance
import com.brkckr.fakeiglive.core.ui.theme.spacing
import com.brkckr.fakeiglive.feature.live.camera.LiveCameraLayer
import com.brkckr.fakeiglive.feature.live.components.BottomLiveBar
import com.brkckr.fakeiglive.feature.live.components.CommentFeed
import com.brkckr.fakeiglive.feature.live.components.FloatingHeartArea
import com.brkckr.fakeiglive.feature.live.components.LiveControlColumn
import com.brkckr.fakeiglive.feature.live.components.TopLiveBar
import kotlinx.coroutines.flow.Flow

@Composable
fun LiveRoute(
    viewModel: LiveViewModel,
    onClose: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LiveUiEffect.ShowError -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    LiveScreen(
        state = uiState,
        heartEvents = viewModel.heartEvents,
        onIntent = viewModel::onIntent,
        onClose = onClose,
    )
}

@Composable
fun LiveScreen(
    state: LiveUiState,
    heartEvents: Flow<Long>,
    onIntent: (LiveIntent) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SystemBarsAppearance(useDarkIcons = false)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        LiveCameraLayer(
            isCameraEnabled = state.isCameraEnabled,
            lensFacing = state.cameraLensFacing,
            onEnableCamera = { onIntent(LiveIntent.ToggleCamera) },
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.48f),
                        0.22f to Color.Transparent,
                        0.56f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.72f),
                    ),
                ),
        )

        if (state.areEffectsEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFF4F9A).copy(alpha = 0.055f)),
            )
        }

        TopLiveBar(
            profile = state.profile,
            viewerCount = state.viewerCount,
            onClose = onClose,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
        )

        LiveControlColumn(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 72.dp, end = 7.dp),
        )

        CommentFeed(
            comments = state.comments,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(
                    start = MaterialTheme.spacing.medium - 2.dp,
                    bottom = 80.dp
                ) // increased from 72dp for better bar clearance
                .fillMaxWidth(0.76f)
                .height(280.dp), // slightly increased height
        )

        FloatingHeartArea(
            heartEvents = heartEvents,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 70.dp, end = 3.dp)
                .width(126.dp)
                .height(390.dp),
        )

        BottomLiveBar(
            onSendHeart = { onIntent(LiveIntent.SendHeart) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars),
        )
    }
}
