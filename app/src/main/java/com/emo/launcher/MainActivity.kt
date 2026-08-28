package com.emo.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.emo.launcher.ui.EmoLauncherApp
import com.emo.launcher.ui.theme.LauncherTheme

class MainActivity : ComponentActivity() {

    private val viewModel:
        LauncherViewModel by viewModels()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        enableEdgeToEdge()

        setContent {
            LauncherTheme {

                val apps by
                    viewModel.apps.collectAsState()

                val homeItems by
                    viewModel.homeItems.collectAsState()

                val settings by
                    viewModel.settings.collectAsState()

                EmoLauncherApp(
                    apps = apps,
                    homeItems = homeItems,
                    settings = settings,
                    onLaunchApp =
                        viewModel::launch,
                    onUpdateGrid =
                        viewModel::updateGrid,
                    onIconSize =
                        viewModel::updateIconSize,
                    onLabelSize =
                        viewModel::updateLabelSize,
                    onShowLabels =
                        viewModel::updateShowLabels,
                    onShowDock =
                        viewModel::updateShowDock,
                    onDoubleTapLock = {
                        viewModel
                            .handleDoubleTapLock(
                                this
                            )
                    },
                    onMoveHomeApp =
                        viewModel::moveHomeApp
                )
            }
        }
    }
}
