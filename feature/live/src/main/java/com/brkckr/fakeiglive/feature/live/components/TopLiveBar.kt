package com.brkckr.fakeiglive.feature.live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brkckr.fakeiglive.core.extensions.toCompactViewerCount
import com.brkckr.fakeiglive.core.ui.components.LiveBadge
import com.brkckr.fakeiglive.core.ui.components.ProfileAvatar
import com.brkckr.fakeiglive.core.ui.theme.spacing
import com.brkckr.fakeiglive.domain.model.UserProfile
import com.brkckr.fakeiglive.feature.live.R

@Composable
fun TopLiveBar(
    profile: UserProfile,
    viewerCount: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.small + 2.dp, vertical = MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileAvatar(
            imageUri = profile.profileImageUri,
            username = profile.username,
            modifier = Modifier.size(42.dp),
            borderWidth = MaterialTheme.spacing.extraSmall / 2,
        )
        Spacer(Modifier.width(MaterialTheme.spacing.small))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = profile.username,
                modifier = Modifier.weight(1f, fill = false),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(19.dp),
                tint = Color.White,
            )
        }
        Spacer(Modifier.width(MaterialTheme.spacing.small - 1.dp))
        LiveBadge()
        Spacer(Modifier.width(MaterialTheme.spacing.small - 2.dp))
        Row(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.38f), RoundedCornerShape(MaterialTheme.spacing.extraSmall))
                .padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.small - 1.dp),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = stringResource(R.string.live_viewer_count),
                modifier = Modifier.size(16.dp),
                tint = Color.White,
            )
            Text(
                text = viewerCount.toCompactViewerCount(),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        IconButton(
            onClick = onClose,
            modifier = Modifier.size(42.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.live_close_live),
                modifier = Modifier.size(31.dp),
                tint = Color.White,
            )
        }
    }
}
