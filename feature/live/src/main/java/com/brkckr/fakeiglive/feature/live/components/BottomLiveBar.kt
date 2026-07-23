package com.brkckr.fakeiglive.feature.live.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brkckr.fakeiglive.core.ui.theme.spacing
import com.brkckr.fakeiglive.feature.live.R

@Composable
fun BottomLiveBar(
    onSendHeart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.small + 4.dp, vertical = MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(MaterialTheme.spacing.huge - 4.dp) // 44dp
                .background(Color.Black.copy(alpha = 0.18f), RoundedCornerShape(MaterialTheme.spacing.large))
                .border(
                    BorderStroke(MaterialTheme.spacing.none + 1.dp, Color.White.copy(alpha = 0.72f)),
                    RoundedCornerShape(MaterialTheme.spacing.large),
                )
                .padding(horizontal = MaterialTheme.spacing.medium),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = stringResource(R.string.live_add_comment),
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 14.sp,
            )
        }
        Spacer(Modifier.width(MaterialTheme.spacing.extraSmall / 2)) // 2dp
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                contentDescription = stringResource(R.string.live_question),
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.spacing.extraLarge + 3.dp), // 27dp
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = stringResource(R.string.live_send_comment),
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.spacing.extraLarge + 2.dp), // 26dp
            )
        }
        IconButton(onClick = onSendHeart) {
            Icon(
                imageVector = Icons.Rounded.FavoriteBorder,
                contentDescription = stringResource(R.string.live_send_heart),
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.spacing.extraLarge + 5.dp), // 29dp
            )
        }
    }
}
