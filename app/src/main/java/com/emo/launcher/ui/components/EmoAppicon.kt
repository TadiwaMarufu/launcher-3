package com.emo.launcher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

@Composable
fun EmoAppIcon(
    icon: Drawable?,
    modifier: Modifier = Modifier,
    iconSize: Float = 52f
) {
    Box(
        modifier = modifier
            .size(iconSize.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        icon?.let {

            Image(
                bitmap = it.toBitmap(
                    width = 96,
                    height = 96
                ).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size((iconSize * 0.81f).dp)
            )
        }
    }
}