package com.emo.launcher.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emo.launcher.model.LauncherSettings

@Composable
fun HomeCustomizationBar(
    visible: Boolean,
    settings: LauncherSettings,
    onWallpaper: () -> Unit,
    onWidgets: () -> Unit,
    onApps: () -> Unit,
    onSettings: () -> Unit,
    onUpdateGrid: (Int, Int) -> Unit,
    onIconSize: (Float) -> Unit,
    onLabelSize: (Float) -> Unit,
    onShowLabels: (Boolean) -> Unit,
    onShowDock: (Boolean) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLayoutSheet by remember {
        mutableStateOf(false)
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomizationAction(
                    icon = Icons.Default.Wallpaper,
                    label = "Wallpaper",
                    onClick = onWallpaper
                )

                CustomizationAction(
                    icon = Icons.Default.Widgets,
                    label = "Widgets",
                    onClick = onWidgets
                )

                CustomizationAction(
                    icon = Icons.Default.GridView,
                    label = "Layout",
                    onClick = {
                        showLayoutSheet = true
                    }
                )

                CustomizationAction(
                    icon = Icons.Default.Apps,
                    label = "Apps",
                    onClick = onApps
                )

                CustomizationAction(
                    icon = Icons.Default.Settings,
                    label = "Settings",
                    onClick = onSettings
                )

                TextButton(
                    onClick = onDone
                ) {
                    Text("Done")
                }
            }
        }
    }

    if (showLayoutSheet) {
        LayoutCustomizationSheet(
            settings = settings,
            onDismiss = {
                showLayoutSheet = false
            },
            onUpdateGrid = onUpdateGrid,
            onIconSize = onIconSize,
            onLabelSize = onLabelSize,
            onShowLabels = onShowLabels,
            onShowDock = onShowDock
        )
    }
}

@Composable
private fun CustomizationAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label
            )

            Text(label)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LayoutCustomizationSheet(
    settings: LauncherSettings,
    onDismiss: () -> Unit,
    onUpdateGrid: (Int, Int) -> Unit,
    onIconSize: (Float) -> Unit,
    onLabelSize: (Float) -> Unit,
    onShowLabels: (Boolean) -> Unit,
    onShowDock: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 12.dp
                )
        ) {
            Text(
                text = "Home layout"
            )

            Text(
                text = "${settings.gridColumns} × ${settings.gridRows}"
            )

            Slider(
                value = settings.gridColumns.toFloat(),
                onValueChange = {
                    onUpdateGrid(
                        it.toInt().coerceIn(3, 7),
                        settings.gridRows
                    )
                },
                valueRange = 3f..7f,
                steps = 3
            )

            Text(
                text = "Rows: ${settings.gridRows}"
            )

            Slider(
                value = settings.gridRows.toFloat(),
                onValueChange = {
                    onUpdateGrid(
                        settings.gridColumns,
                        it.toInt().coerceIn(4, 10)
                    )
                },
                valueRange = 4f..10f,
                steps = 5
            )

            Text(
                text = "Icon size"
            )

            Slider(
                value = settings.iconSize,
                onValueChange = onIconSize,
                valueRange = 0.75f..1.5f
            )

            Text(
                text = "Label size"
            )

            Slider(
                value = settings.labelSize,
                onValueChange = onLabelSize,
                valueRange = 0.75f..1.5f
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Show labels")

                Switch(
                    checked = settings.showLabels,
                    onCheckedChange = onShowLabels
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Show dock")

                Switch(
                    checked = settings.showDock,
                    onCheckedChange = onShowDock
                )
            }

            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
