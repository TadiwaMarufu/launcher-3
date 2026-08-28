package com.emo.launcher

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.emo.launcher.security.EmoDeviceAdminReceiver
import com.emo.launcher.ui.EmoLauncherApp
import com.emo.launcher.ui.theme.LauncherTheme

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    private val devicePolicyManager by lazy {
        getSystemService(
            DevicePolicyManager::class.java
        )
    }

    private val adminComponent by lazy {
        ComponentName(
            this,
            EmoDeviceAdminReceiver::class.java
        )
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LauncherTheme {
                val apps by
                    viewModel.apps.collectAsState()

                val settings by
                    viewModel.settings.collectAsState()

                EmoLauncherApp(
                    apps = apps,
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
                    onDoubleTapLock =
                        ::handleDoubleTapLock
                )
            }
        }
    }

    private fun handleDoubleTapLock() {
        if (
            devicePolicyManager.isAdminActive(
                adminComponent
            )
        ) {
            devicePolicyManager.lockNow()
            return
        }

        val intent = Intent(
            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN
        ).apply {
            putExtra(
                DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                adminComponent
            )

            putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "EmoLauncher uses device administrator access only to lock the screen when you double-tap the home screen."
            )
        }

        startActivity(intent)
    }
}
