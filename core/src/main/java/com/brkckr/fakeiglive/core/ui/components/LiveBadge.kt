package com.brkckr.fakeiglive.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brkckr.fakeiglive.core.R
import com.brkckr.fakeiglive.core.ui.theme.InstagramPink
import com.brkckr.fakeiglive.core.ui.theme.spacing

@Composable
fun LiveBadge(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 7.dp,
    elevation: Dp = MaterialTheme.spacing.none,
) {
    if (elevation > MaterialTheme.spacing.none) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(MaterialTheme.spacing.extraSmall),
            color = InstagramPink,
            shadowElevation = elevation,
        ) {
            BadgeText(fontSize, horizontalPadding, verticalPadding)
        }
    } else {
        Box(
            modifier = modifier
                .background(InstagramPink, RoundedCornerShape(MaterialTheme.spacing.extraSmall))
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            contentAlignment = Alignment.Center,
        ) {
            BadgeText(fontSize, horizontalPadding, verticalPadding = verticalPadding)
        }
    }
}

@Composable
private fun BadgeText(
    fontSize: TextUnit,
    horizontalPadding: Dp,
    verticalPadding: Dp,
) {
    Text(
        text = stringResource(R.string.core_live),
        modifier = if (verticalPadding == MaterialTheme.spacing.none) Modifier.padding(horizontal = horizontalPadding, vertical = MaterialTheme.spacing.small) else Modifier,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
    )
}
