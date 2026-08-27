package com.emo.launcher.ui

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emo.launcher.model.AppInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EmoLauncherApp(viewModel: LauncherViewModel = viewModel()) {
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val drawerOpen by viewModel.drawerOpen.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    val now = Date()
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
    val date = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(now)

    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme(
            background = Color.Black,
            surface = Color(0xFF111111),
            surfaceContainer = Color(0xFF181818),
            primary = Color.White,
            onPrimary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(drawerOpen) {
                    detectTapGestures(
                        onLongPress = {
                            // Customization surface is introduced in Milestone 2.
                        },
                        onDoubleTap = {
                            // Lock-screen action is intentionally not wired until
                            // the launcher has a secure DevicePolicy-compatible path.
                        }
                    )
                },
            color = Color.Black
        ) {
            AnimatedContent(
                targetState = drawerOpen,
                transitionSpec = {
                    (fadeIn(spring()) + scaleIn(initialScale = 0.98f)) togetherWith
                        (fadeOut(spring()) + scaleOut(targetScale = 1.02f))
                },
                label = "launcher_surface"
            ) { open ->
                if (open) {
                    Drawer(
                        apps = apps,
                        query = query,
                        onQuery = viewModel::setQuery,
                        onLaunch = viewModel::launch,
                        onClose = viewModel::closeDrawer
                    )
                } else {
                    Home(
                        time = time,
                        date = date,
                        onOpenDrawer = viewModel::openDrawer
                    )
                }
            }
        }
    }
}

@Composable
private fun Home(
    time: String,
    date: String,
    onOpenDrawer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = time,
                fontSize = 64.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-2).sp
            )
            Text(
                text = date,
                color = Color(0xFFBDBDBD),
                fontSize = 16.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .clip(RoundedCornerShape(29.dp))
                    .clickable(onClick = onOpenDrawer),
                color = Color(0xFF171717),
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search apps",
                        tint = Color(0xFFBDBDBD)
                    )
                    Spacer(Modifier.size(12.dp))
                    Text(
                        "Search apps",
                        color = Color(0xFF9E9E9E),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Swipe up for apps",
                color = Color(0xFF666666),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun Drawer(
    apps: List<AppInfo>,
    query: String,
    onQuery: (String) -> Unit,
    onLaunch: (AppInfo) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp)
            .padding(top = 52.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQuery,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null
                )
            },
            placeholder = { Text("Search anything") },
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF181818),
                unfocusedContainerColor = Color(0xFF181818),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (query.isBlank()) "All apps" else "Results",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Launcher settings",
                tint = Color(0xFF777777),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        if (apps.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No matching apps",
                    color = Color(0xFF888888)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 78.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = if (query.isBlank()) apps else apps.filter {
                        val q = query.lowercase()
                        it.label.lowercase().contains(q) ||
                            it.packageName.lowercase().contains(q)
                    },
                    key = { "${it.packageName}/${it.activityName}" }
                ) { app ->
                    AppTile(app = app, onClick = { onLaunch(app) })
                }
            }
        }
    }
}

@Composable
private fun AppTile(
    app: AppInfo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidDrawableIcon(
            drawable = app.icon,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(7.dp))
        Text(
            text = app.label,
            fontSize = 12.sp,
            color = Color(0xFFE5E5E5),
            maxLines = 1
        )
    }
}

@Composable
private fun AndroidDrawableIcon(
    drawable: Drawable,
    modifier: Modifier = Modifier
) {
    val bitmap: ImageBitmap = drawable.toBitmap(96, 96).asImageBitmap()
    androidx.compose.foundation.Image(
        bitmap = bitmap,
        contentDescription = null,
        modifier = modifier.clip(RoundedCornerShape(14.dp)),
        contentScale = ContentScale.Fit
    )
}
