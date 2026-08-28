package com.emo.launcher.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emo.launcher.data.AppRepository
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    repository: AppRepository,
    onOpenDrawer: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    var customizationMode by remember { mutableStateOf(false) }
    var dragging by remember { mutableStateOf(false) }

    val dragOffsetX = remember { Animatable(0f) }
    val dragOffsetY = remember { Animatable(0f) }

    LaunchedEffect(customizationMode) {
        if (!customizationMode) {
            dragOffsetX.animateTo(0f, spring())
            dragOffsetY.animateTo(0f, spring())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        if (customizationMode) {
                            dragging = true
                        }
                    },
                    onDrag = { change, dragAmount ->
                        if (customizationMode) {
                            dragOffsetX.snapTo(dragOffsetX.value + dragAmount.x)
                            dragOffsetY.snapTo(dragOffsetY.value + dragAmount.y)
                            change.positionChange()
                        }
                    },
                    onDragEnd = {
                        dragging = false
                    },
                    onDragCancel = {
                        dragging = false
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        dragOffsetX.value.roundToInt(),
                        dragOffsetY.value.roundToInt()
                    )
                }
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "11:42",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 64.sp
            )

            Text(
                text = "Thursday, 27 August",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(28.dp))

            SearchSurface(
                onClick = onOpenSearch
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = !customizationMode
            ) {
                HomeWidgetSurface()
            }

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            Dock(
                repository = repository,
                onOpenDrawer = onOpenDrawer
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        if (customizationMode) {
            CustomizationBar(
                onDone = {
                    customizationMode = false
                }
            )
        }

        IconButton(
            onClick = {
                customizationMode = !customizationMode
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Customize launcher"
            )
        }
    }
}

@Composable
private fun SearchSurface(
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = "Search anything…",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun HomeWidgetSurface() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "24°",
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "Harare",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Clear skies",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun Dock(
    repository: AppRepository,
    onOpenDrawer: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(38.dp)),
        shape = RoundedCornerShape(38.dp),
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DockButton("Phone")
            DockButton("Browser")
            DockButton("Apps", onOpenDrawer)
            DockButton("Music")
        }
    }
}

@Composable
private fun DockButton(
    label: String,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(58.dp)
    ) {
        Text(
            text = label.take(1),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun CustomizationBar(
    onDone: () -> Unit
) {
    Surface(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Wallpaper")
            Text("Widgets")
            Text("Layout")
            Text("Done")

            IconButton(onClick = onDone) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Done"
                )
            }
        }
    }
}
