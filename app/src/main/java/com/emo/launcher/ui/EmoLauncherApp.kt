@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.emo.launcher.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.emo.launcher.model.AppInfo
import com.emo.launcher.model.LauncherSettings
import com.emo.launcher.ui.components.EmoAppIcon
import com.emo.launcher.ui.home.HomeScreen

@Composable
fun EmoLauncherApp(
    apps: List<AppInfo>,
    settings: LauncherSettings,
    onLaunchApp: (AppInfo) -> Unit,
    onUpdateGrid: (Int, Int) -> Unit,
    onIconSize: (Float) -> Unit,
    onLabelSize: (Float) -> Unit,
    onShowLabels: (Boolean) -> Unit,
    onShowDock: (Boolean) -> Unit
) {
    var screen by remember {
        mutableStateOf("home")
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "launcher_screen"
    ) { currentScreen ->

        when (currentScreen) {

            "drawer" -> {
                AppDrawer(
                    apps = apps,
                    onLaunchApp = onLaunchApp,
                    onBack = {
                        screen = "home"
                    }
                )
            }

            "search" -> {
                SearchScreen(
                    apps = apps,
                    onLaunchApp = onLaunchApp,
                    onBack = {
                        screen = "home"
                    }
                )
            }

            "settings" -> {
                LauncherSettingsScreen(
                    settings = settings,
                    onBack = {
                        screen = "home"
                    },
                    onUpdateGrid = onUpdateGrid,
                    onIconSize = onIconSize,
                    onLabelSize = onLabelSize,
                    onShowLabels = onShowLabels,
                    onShowDock = onShowDock
                )
            }

            else -> {
                HomeScreen(
                    settings = settings,
                    onOpenDrawer = {
                        screen = "drawer"
                    },
                    onOpenSearch = {
                        screen = "search"
                    },
                    onOpenSettings = {
                        screen = "settings"
                    },
                    onOpenWallpaper = {
                        screen = "wallpaper"
                    },
                    onOpenWidgets = {
                        screen = "widgets"
                    },
                    onUpdateGrid = onUpdateGrid,
                    onIconSize = onIconSize,
                    onLabelSize = onLabelSize,
                    onShowLabels = onShowLabels,
                    onShowDock = onShowDock
                )
            }
        }
    }
}

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
                    Text("Apps")
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
                key = {
                    "${it.packageName}/${it.activityName}"
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
private fun SearchScreen(
    apps: List<AppInfo>,
    onLaunchApp: (AppInfo) -> Unit,
    onBack: () -> Unit
) {
    var query by remember {
        mutableStateOf("")
    }

    val normalizedQuery = query
        .trim()
        .lowercase()

    val results = remember(
        normalizedQuery,
        apps
    ) {
        if (normalizedQuery.isEmpty()) {
            apps
        } else {
            apps
                .filter { app ->
                    app.label
                        .lowercase()
                        .contains(normalizedQuery) ||
                    app.packageName
                        .lowercase()
                        .contains(normalizedQuery)
                }
                .sortedBy {
                    it.label.lowercase()
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
                key = {
                    "${it.packageName}/${it.activityName}"
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
            .clip(
                RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 14.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        EmoAppIcon(
            icon = app.icon
        )

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
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LauncherSettingsScreen(
    settings: LauncherSettings,
    onBack: () -> Unit,
    onUpdateGrid: (Int, Int) -> Unit,
    onIconSize: (Float) -> Unit,
    onLabelSize: (Float) -> Unit,
    onShowLabels: (Boolean) -> Unit,
    onShowDock: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("EmoLauncher")
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
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "Appearance",
                    style = MaterialTheme
                        .typography
                        .headlineSmall
                )
            }

            item {
                Text(
                    text =
                        "Home grid: " +
                        "${settings.gridColumns} × " +
                        settings.gridRows
                )
            }

            item {
                Text(
                    text =
                        "Icon scale: " +
                        "%.2f".format(
                            settings.iconSize
                        )
                )
            }

            item {
                Text(
                    text =
                        "Label scale: " +
                        "%.2f".format(
                            settings.labelSize
                        )
                )
            }

            item {
                Text(
                    text =
                        if (settings.showLabels) {
                            "App labels: On"
                        } else {
                            "App labels: Off"
                        }
                )
            }

            item {
                Text(
                    text =
                        if (settings.showDock) {
                            "Dock: On"
                        } else {
                            "Dock: Off"
                        }
                )
            }
        }
    }
}
