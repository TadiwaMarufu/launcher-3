cat > app/src/main/java/com/emo/launcher/MainActivity.kt <<'EOF'
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

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LauncherTheme {
                val apps by viewModel.apps.collectAsState()

                EmoLauncherApp(
                    apps = apps,
                    onLaunchApp = viewModel::launch
                )
            }
        }
    }
}
EOF