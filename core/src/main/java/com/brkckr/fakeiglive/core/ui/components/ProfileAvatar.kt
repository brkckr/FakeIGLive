package com.brkckr.fakeiglive.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.brkckr.fakeiglive.core.ui.theme.InstagramOrange
import com.brkckr.fakeiglive.core.ui.theme.InstagramPink
import com.brkckr.fakeiglive.core.ui.theme.InstagramPurple
import com.brkckr.fakeiglive.core.ui.theme.spacing

private val DefaultAvatarBorder = Brush.sweepGradient(
    listOf(InstagramOrange, InstagramPink, InstagramPurple, InstagramOrange),
)

@Composable
fun ProfileAvatar(
    imageUri: String?,
    username: String,
    modifier: Modifier = Modifier,
    borderWidth: Dp = MaterialTheme.spacing.extraSmall / 2, // default 2dp
    borderBrush: Brush = DefaultAvatarBorder,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(borderBrush)
            .padding(borderWidth)
            .clip(CircleShape)
            .background(Color(0xFF3D3037)),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUri.isNullOrBlank()) {
            AvatarFallback(username)
        } else {
            SubcomposeAsyncImage(
                model = imageUri,
                contentDescription = "$username profile photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = { AvatarFallback(username) },
                error = { AvatarFallback(username) },
                success = { SubcomposeAsyncImageContent() },
            )
        }
    }
}

@Composable
private fun AvatarFallback(username: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF7D5268), Color(0xFF34242C)),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = username.trim().firstOrNull()?.uppercase() ?: "?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}
