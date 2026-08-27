cat > app/src/main/java/com/emo/launcher/ui/home/HomeScreen.kt <<'EOF'
package com.emo.launcher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onOpenSearch: () -> Unit
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
            modifier = Modifier.height(28.dp)
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
}
EOF