package com.emo.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.emo.launcher.ui.EmoLauncherApp
import com.emo.launcher.ui.theme.LauncherTheme

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LauncherTheme {
                EmoLauncherApp(
                    apps = viewModel.apps.collectAsState().value,
                    onLaunchApp = viewModel::launch
                )
            }
        }
    }
}