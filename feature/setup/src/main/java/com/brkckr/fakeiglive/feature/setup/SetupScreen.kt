package com.brkckr.fakeiglive.feature.setup

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brkckr.fakeiglive.core.extensions.SystemBarsAppearance
import com.brkckr.fakeiglive.core.ui.components.InstagramButton
import com.brkckr.fakeiglive.core.ui.components.LiveBadge
import com.brkckr.fakeiglive.core.ui.components.ProfileAvatar
import com.brkckr.fakeiglive.core.ui.theme.InstagramPink
import com.brkckr.fakeiglive.core.ui.theme.InstagramPurple
import com.brkckr.fakeiglive.core.ui.theme.spacing
import com.brkckr.fakeiglive.domain.model.UserProfile
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SetupRoute(
    viewModel: SetupViewModel,
    onStartLive: (UserProfile) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(viewModel, onStartLive) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is SetupUiEffect.NavigateToLive -> onStartLive(effect.profile)
            }
        }
    }

    SetupScreen(
        state = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun SetupScreen(
    state: SetupUiState,
    onIntent: (SetupIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    SystemBarsAppearance(useDarkIcons = !isDark)
    val context = LocalContext.current
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION,
            )
        }
        onIntent(SetupIntent.ProfilePhotoSelected(uri.toString()))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    if (isDark) {
                        listOf(Color(0xFF1A1A1A), Color(0xFF121212))
                    } else {
                        listOf(Color(0xFFFFF8FB), Color(0xFFFFEFF7), Color.White)
                    },
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(230.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            InstagramPink.copy(alpha = if (isDark) 0.08f else 0.16f),
                            Color.Transparent
                        ),
                    ),
                    CircleShape,
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = MaterialTheme.spacing.large,
                    vertical = MaterialTheme.spacing.large
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LiveBadge(
                elevation = MaterialTheme.spacing.small,
                horizontalPadding = 18.dp,
                fontSize = 16.sp,
                verticalPadding = MaterialTheme.spacing.none
            )
            Spacer(Modifier.height(MaterialTheme.spacing.large))
            Text(
                text = stringResource(R.string.setup_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(MaterialTheme.spacing.small))
            Text(
                text = stringResource(R.string.setup_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(34.dp))
            PhotoPicker(
                state = state,
                onClick = { photoPicker.launch(arrayOf("image/*")) },
            )
            Spacer(Modifier.height(MaterialTheme.spacing.large))
            OutlinedTextField(
                value = state.username,
                onValueChange = { onIntent(SetupIntent.UsernameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.setup_username_label)) },
                placeholder = { Text(stringResource(R.string.setup_username_placeholder)) },
                supportingText = {
                    Text(
                        text = state.usernameError?.asString()
                            ?: stringResource(R.string.setup_username_support)
                    )
                },
                isError = state.usernameError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                shape = RoundedCornerShape(MaterialTheme.spacing.medium),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = InstagramPink,
                    focusedLabelColor = InstagramPink,
                ),
            )
            Spacer(Modifier.height(MaterialTheme.spacing.medium))
            InstagramButton(
                text = stringResource(R.string.setup_start_live),
                onClick = { onIntent(SetupIntent.StartLiveClicked) },
                enabled = state.isFormValid,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(MaterialTheme.spacing.medium))
            Text(
                text = stringResource(R.string.setup_privacy_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PhotoPicker(
    state: SetupUiState,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            ProfileAvatar(
                imageUri = state.profileImageUri,
                username = state.username,
                modifier = Modifier.size(112.dp),
                borderWidth = MaterialTheme.spacing.extraSmall, // 4dp
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(MaterialTheme.spacing.extraLarge + 4.dp), // 36dp
                shape = CircleShape,
                color = InstagramPink,
                shadowElevation = MaterialTheme.spacing.extraSmall,
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddAPhoto,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                )
            }
        }
        Spacer(Modifier.height(MaterialTheme.spacing.small + 2.dp)) // 10dp
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(
                    if (state.profileImageUri.isBlank()) {
                        R.string.setup_choose_profile_photo
                    } else {
                        R.string.setup_change_profile_photo
                    },
                ),
                color = if (state.showPhotoWarning) MaterialTheme.colorScheme.error else InstagramPurple,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (state.showPhotoWarning) {
            Text(
                text = stringResource(R.string.setup_error_photo_required),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
