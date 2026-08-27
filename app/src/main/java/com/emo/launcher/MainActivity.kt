package com.emo.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.emo.launcher.ui.EmoLauncherApp

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val apps by viewModel.apps.collectAsState()

            MaterialTheme {

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmoLauncherApp(
                        apps = apps,
                        onLaunchApp = viewModel::launch
                    )
                }
            }
        }
    }
}
