package com.brkckr.fakeiglive.feature.live.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.brkckr.fakeiglive.core.ui.theme.spacing
import kotlinx.coroutines.flow.Flow
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private data class HeartParticle(
    val id: Long, // unique identifier for key-based composition
    val xFraction: Float, // horizontal spawn position (0.0 to 1.0)
    val sizeDp: Int, // visual size of the heart icon
    val durationMs: Int, // time taken to complete flight animation
    val sway: Float, // horizontal oscillation amplitude
    val rotation: Float, // initial tilt angle
    val color: Color, // tinted color of the heart
)

// handle high-frequency particle animation logic
@Composable
fun FloatingHeartArea(
    heartEvents: Flow<Long>,
    modifier: Modifier = Modifier,
) {
    val particles = remember { mutableStateListOf<HeartParticle>() }

    LaunchedEffect(heartEvents) {
        heartEvents.collect { id ->
            if (particles.size >= MAX_HEARTS) particles.removeAt(0)
            particles += HeartParticle(
                id = id,
                xFraction = Random.nextFloat() * 0.54f + 0.18f,
                sizeDp = Random.nextInt(28, 47),
                durationMs = Random.nextInt(2_500, 3_900),
                sway = Random.nextFloat() * 2f - 1f,
                rotation = Random.nextFloat() * 24f - 12f,
                color = HEART_COLORS.random(),
            )
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        particles.forEach { particle ->
            key(particle.id) {
                AnimatedHeart(
                    particle = particle,
                    areaWidthPx = widthPx,
                    areaHeightPx = heightPx,
                    onFinished = { particles.remove(particle) },
                )
            }
        }
    }
}

@Composable
private fun AnimatedHeart(
    particle: HeartParticle,
    areaWidthPx: Float,
    areaHeightPx: Float,
    onFinished: () -> Unit,
) {
    val progress = remember { Animatable(0f) }
    val density = LocalDensity.current
    val size = particle.sizeDp.dp
    val sizePx = with(density) { size.toPx() }

    LaunchedEffect(particle.id) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = particle.durationMs,
                easing = LinearEasing,
            ),
        )
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = null,
            tint = particle.color,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(size)
                .graphicsLayer {
                    val value = progress.value
                    val fadeIn = (value / 0.12f).coerceIn(0f, 1f)
                    val fadeOut = ((1f - value) / 0.28f).coerceIn(0f, 1f)
                    translationX = areaWidthPx * particle.xFraction +
                        sin(value * PI.toFloat() * 3f) * 25f * particle.sway
                    translationY = -value * (areaHeightPx + sizePx)
                    alpha = minOf(fadeIn, fadeOut)
                    scaleX = 0.72f + value.coerceAtMost(0.28f)
                    scaleY = scaleX
                    rotationZ = particle.rotation * value
                    shadowElevation = 10f
                    transformOrigin = TransformOrigin.Center
                },
        )
    }
}

private const val MAX_HEARTS = 18
private val HEART_COLORS = listOf(
    Color(0xFFFF2D55),
    Color(0xFFFF1744),
    Color(0xFFE60023),
    Color(0xFFFF4D67),
)
