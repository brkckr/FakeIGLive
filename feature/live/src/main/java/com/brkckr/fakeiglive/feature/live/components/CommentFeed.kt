package com.brkckr.fakeiglive.feature.live.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brkckr.fakeiglive.core.ui.components.ProfileAvatar
import com.brkckr.fakeiglive.core.ui.theme.spacing
import com.brkckr.fakeiglive.domain.model.Comment
import com.brkckr.fakeiglive.feature.live.LiveComment
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CommentFeed(
    comments: ImmutableList<LiveComment>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    // auto-scroll to the latest comment when a new one arrives
    LaunchedEffect(comments.size) {
        if (comments.isNotEmpty()) {
            // using scrollToItem first to jump, then maybe animate for smoothness if needed
            // but for high-frequency live chats, immediate jump or short animation is better
            listState.animateScrollToItem(0)
        }
    }

    LazyColumn(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                // apply fading edge effect to the top of the feed (where old comments go)
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.15f to Color.Black // slightly sharper fade to keep more area clear
                    ),
                    blendMode = BlendMode.DstIn
                )
            },
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(
            top = MaterialTheme.spacing.small,
            bottom = MaterialTheme.spacing.medium // increased bottom padding for better visibility of the newest item
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small + 2.dp), // 10dp
    ) {
        items(
            items = comments,
            key = LiveComment::id,
        ) { comment ->
            CommentItem(
                comment = comment.content,
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(durationMillis = COMMENT_ENTER_DURATION_MS),
                    placementSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                    fadeOutSpec = tween(durationMillis = COMMENT_EXIT_DURATION_MS),
                ),
            )
        }
    }
}

private const val COMMENT_ENTER_DURATION_MS = 420
private const val COMMENT_EXIT_DURATION_MS = 320

@Composable
private fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileAvatar(
            imageUri = comment.profileImageUri,
            username = comment.username,
            modifier = Modifier.size(MaterialTheme.spacing.massive - 28.dp), // 36dp
            borderWidth = MaterialTheme.spacing.none + 1.dp,
        )
        Spacer(Modifier.width(MaterialTheme.spacing.small + 1.dp)) // 9dp
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(comment.username)
                }
                append("  ")
                append(comment.text)
            },
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall / 2), // 2dp
            color = Color.White,
            fontSize = 15.sp,
            lineHeight = 19.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    blurRadius = 5f,
                ),
            ),
        )
    }
}
