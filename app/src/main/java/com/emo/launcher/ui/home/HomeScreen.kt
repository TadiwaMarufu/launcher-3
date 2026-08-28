package com.emo.launcher.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emo.launcher.model.AppInfo
import com.emo.launcher.model.LauncherSettings
import com.emo.launcher.ui.components.EmoAppIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    apps: List<AppInfo>,
    settings: LauncherSettings,
    onLaunchApp: (AppInfo) -> Unit,
    onOpenDrawer: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenWallpaper: () -> Unit,
    onOpenWidgets: () -> Unit,
    onDoubleTapLock: () -> Unit,
    onUpdateGrid: (Int, Int) -> Unit,
    onIconSize: (Float) -> Unit,
    onLabelSize: (Float) -> Unit,
    onShowLabels: (Boolean) -> Unit,
    onShowDock: (Boolean) -> Unit
) {
    var customizationMode by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .emoHomeGestures(
                onDoubleTap = {
                    if (!customizationMode) {
                        onDoubleTapLock()
                    }
                },
                onLongPress = {
                    customizationMode = true
                },
                onSwipeUp = {
                    if (!customizationMode) {
                        onOpenDrawer()
                    }
                },
                onSwipeDown = {
                    if (!customizationMode) {
                        onOpenSearch()
                    }
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 18.dp,
                    vertical = 24.dp
                )
        ) {
            LauncherClock()

            LauncherSearchBar(
                onClick = onOpenSearch
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(
                    settings.gridColumns
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        top = 18.dp,
                        bottom = 12.dp
                    ),
                contentPadding = PaddingValues(
                    horizontal = 4.dp,
                    vertical = 8.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = apps.take(
                        settings.gridColumns *
                            settings.gridRows
                    ),
                    key = {
                        "${it.packageName}/${it.activityName}"
                    }
                ) { app ->

                    HomeAppItem(
                        app = app,
                        settings = settings,
                        customizationMode = customizationMode,
                        onClick = {
                            if (!customizationMode) {
                                onLaunchApp(app)
                            }
                        },
                        onLongClick = {
                            customizationMode = true
                        }
                    )
                }
            }

            if (settings.showDock) {
                LauncherDock(
                    apps = apps,
                    dockSize = settings.dockSize,
                    iconSize = settings.iconSize,
                    showLabels = settings.showLabels,
                    onLaunchApp = onLaunchApp,
                    onOpenDrawer = onOpenDrawer
                )
            }
        }

        HomeCustomizationBar(
            visible = customizationMode,
            settings = settings,
            onWallpaper = onOpenWallpaper,
            onWidgets = onOpenWidgets,
            onApps = {
                customizationMode = false
                onOpenDrawer()
            },
            onSettings = onOpenSettings,
            onUpdateGrid = onUpdateGrid,
            onIconSize = onIconSize,
            onLabelSize = onLabelSize,
            onShowLabels = onShowLabels,
            onShowDock = onShowDock,
            onDone = {
                customizationMode = false
            },
            modifier = Modifier.align(
                Alignment.BottomCenter
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeAppItem(
    app: AppInfo,
    settings: LauncherSettings,
    customizationMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmoAppIcon(
            icon = app.icon,
            modifier = Modifier
        )

        if (settings.showLabels) {
            androidx.compose.material3.Text(
                text = app.label,
                fontSize = (
                    12f * settings.labelSize
                ).sp,
                maxLines = 1
            )
        }
    }
}
