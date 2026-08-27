package com.emo.launcher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onOpenSearch: () -> Unit
) {
    var customizationMode by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .emoHomeGestures(
                onDoubleTap = {
                    // Reserved for lock-screen integration.
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
                    horizontal = 22.dp,
                    vertical = 28.dp
                ),
            verticalArrangement = Arrangement.Top
        ) {
            LauncherClock()

            Spacer(
                modifier = Modifier.weight(0.04f)
            )

            LauncherSearchBar(
                onClick = onOpenSearch
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            LauncherDock(
                onOpenDrawer = onOpenDrawer
            )
        }

        HomeCustomizationBar(
            visible = customizationMode,
            onWallpaper = {
                // Wallpaper picker will be connected next.
            },
            onWidgets = {
                // AppWidgetHost picker will be connected next.
            },
            onApps = {
                customizationMode = false
                onOpenDrawer()
            },
            onDone = {
                customizationMode = false
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
