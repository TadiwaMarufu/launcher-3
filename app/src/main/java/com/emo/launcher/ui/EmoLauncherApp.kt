package com.emo.launcher.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emo.launcher.model.AppInfo

@Composable
fun EmoLauncherApp(
    apps: List<AppInfo>,
    onLaunchApp: (AppInfo) -> Unit
) {
    var showDrawer by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = when {
            showSearch -> "search"
            showDrawer -> "drawer"
            else -> "home"
        },
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "launcher_screen"
    ) { screen ->

        when (screen) {
            "search" -> {
                SearchScreen(
                    apps = apps,
                    onLaunchApp = onLaunchApp,
                    onBack = {
                        showSearch = false
                    }
                )
            }

            "drawer" -> {
                AppDrawer(
                    apps = apps,
                    onLaunchApp = onLaunchApp,
                    onBack = {
                        showDrawer = false
                    }
                )
            }

            else -> {
                HomeScreen(
                    onOpenDrawer = {
                        showDrawer = true
                    },
                    onOpenSearch = {
                        showSearch = true
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onOpenSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(72.dp)
            )

            Text(
                text = "11:42",
                fontSize = 64.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Thursday, 27 August",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.65f
                )
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            SearchBar(
                onClick = onOpenSearch
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Dock(
                onOpenDrawer = onOpenDrawer
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }
    }
}

@Composable
private fun SearchBar(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = 0.75f
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Text(
                text = "Search anything...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun Dock(
    onOpenDrawer: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface.copy(
            alpha = 0.85f
        ),
        tonalElevation = 4.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            DockItem(
                label = "Apps",
                onClick = onOpenDrawer
            )

            DockItem(
                label = "Phone"
            )

            DockItem(
                label = "Browser"
            )

            DockItem(
                label = "Music"
            )
        }
    }
}

@Composable
private fun DockItem(
    label: String,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                enabled = onClick != null
            ) {
                onClick?.invoke()
            }
            .padding(8.dp)
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = label.take(1),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppDrawer(
    apps: List<AppInfo>,
    onLaunchApp: (AppInfo) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Apps"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {

            items(
                items = apps,
                key = { app ->
                    app.packageName
                }
            ) { app ->

                AppRow(
                    app = app,
                    onClick = {
                        onLaunchApp(app)
                    }
                )
            }
        }
    }
}

@Composable
private fun AppRow(
    app: AppInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 14.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = app.label
                    .firstOrNull()
                    ?.uppercase()
                    ?: "?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = app.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = app.packageName,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    apps: List<AppInfo>,
    onLaunchApp: (AppInfo) -> Unit,
    onBack: () -> Unit
) {
    var query by remember {
        mutableStateOf("")
    }

    val results = remember(
        query,
        apps
    ) {
        if (query.isBlank()) {
            apps
        } else {
            val normalized = query.trim().lowercase()

            apps.filter { app ->
                app.label.lowercase().contains(normalized) ||
                    app.packageName.lowercase().contains(normalized)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Search apps")
                        },
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.padding(
                            end = 12.dp
                        )
                    )
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {

            items(
                items = results,
                key = { app ->
                    app.packageName
                }
            ) { app ->

                AppRow(
                    app = app,
                    onClick = {
                        onLaunchApp(app)
                    }
                )
            }
        }
    }
}
