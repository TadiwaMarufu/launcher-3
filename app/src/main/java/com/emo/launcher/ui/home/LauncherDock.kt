package com.emo.launcher.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emo.launcher.model.AppInfo
import com.emo.launcher.ui.components.EmoAppIcon
import com.emo.launcher.ui.components.EmoGlassSurface

@Composable
fun LauncherDock(
    apps: List<AppInfo>,
    dockSize: Int,
    iconSize: Float,
    showLabels: Boolean,
    onLaunchApp: (AppInfo) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val dockApps = apps
        .filterNot {
            it.label.equals("EmoLauncher", true)
        }
        .take(
            (dockSize - 1).coerceAtLeast(0)
        )

    EmoGlassSurface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            dockApps.forEach { app ->
                DockAppItem(
                    app = app,
                    iconSize = iconSize,
                    showLabel = showLabels,
                    onClick = {
                        onLaunchApp(app)
                    }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable {
                        onOpenDrawer()
                    }
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Apps,
                    contentDescription = "All apps",
                    modifier = Modifier.size(42.dp)
                )

                if (showLabels) {
                    Text(
                        text = "Apps",
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DockAppItem(
    app: AppInfo,
    iconSize: Float,
    showLabel: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(5.dp)
    ) {
        EmoAppIcon(
            icon = app.icon,
            modifier = Modifier
        )

        if (showLabel) {
            Text(
                text = app.label,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}
