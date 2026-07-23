package com.brkckr.fakeiglive.feature.live.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.brkckr.fakeiglive.feature.live.CameraLensFacing
import com.brkckr.fakeiglive.feature.live.R

// manage camera hardware integration and lifecycle
@Composable
fun LiveCameraLayer(
    isCameraEnabled: Boolean,
    lensFacing: CameraLensFacing,
    onEnableCamera: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED,
        )
    }
    var previewFailed by remember(lensFacing) { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    when {
        !isCameraEnabled -> CameraFallback(
            message = stringResource(R.string.live_camera_off),
            actionLabel = stringResource(R.string.live_turn_camera_on),
            onAction = onEnableCamera,
            modifier = modifier,
        )

        !hasPermission -> CameraFallback(
            message = stringResource(R.string.live_camera_permission_needed),
            actionLabel = stringResource(R.string.live_enable_camera),
            onAction = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            modifier = modifier,
        )

        previewFailed -> CameraFallback(
            message = stringResource(R.string.live_camera_unavailable),
            modifier = modifier,
        )

        else -> CameraPreview(
            lensFacing = lensFacing,
            onFailure = { previewFailed = true },
            modifier = modifier,
        )
    }
}

@Composable
private fun CameraPreview(
    lensFacing: CameraLensFacing,
    onFailure: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = remember(context) { LifecycleCameraController(context) }

    LaunchedEffect(controller, lensFacing) {
        runCatching {
            controller.cameraSelector = when (lensFacing) {
                CameraLensFacing.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                CameraLensFacing.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
            }
        }.onFailure { onFailure() }
    }

    DisposableEffect(controller, lifecycleOwner) {
        runCatching { controller.bindToLifecycle(lifecycleOwner) }
            .onFailure { onFailure() }
        onDispose { controller.unbind() }
    }

    AndroidView(
        factory = { viewContext ->
            PreviewView(viewContext).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                this.controller = controller
            }
        },
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun CameraFallback(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF3D2634), Color(0xFF130F12)),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.VideocamOff,
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                    tint = Color.White,
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            if (actionLabel != null && onAction != null) {
                Spacer(Modifier.height(16.dp))
                Button(onClick = onAction) {
                    Text(actionLabel)
                }
            }
        }
    }
}
