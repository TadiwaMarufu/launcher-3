package com.emo.launcher.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emo.launcher.model.LauncherSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.launcherDataStore by preferencesDataStore(
    name = "emo_launcher_preferences"
)

class LauncherPreferences(
    private val context: Context
) {

    private object Keys {
        val GRID_COLUMNS = intPreferencesKey("grid_columns")
        val GRID_ROWS = intPreferencesKey("grid_rows")
        val ICON_SIZE = floatPreferencesKey("icon_size")
        val LABEL_SIZE = floatPreferencesKey("label_size")
        val SHOW_LABELS = booleanPreferencesKey("show_labels")
        val SHOW_DOCK = booleanPreferencesKey("show_dock")
        val DOCK_SIZE = intPreferencesKey("dock_size")
        val HAPTIC = booleanPreferencesKey("haptic")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
    }

    val settings: Flow<LauncherSettings> =
        context.launcherDataStore.data.map { preferences ->

            LauncherSettings(
                gridColumns =
                    preferences[Keys.GRID_COLUMNS] ?: 4,

                gridRows =
                    preferences[Keys.GRID_ROWS] ?: 6,

                iconSize =
                    preferences[Keys.ICON_SIZE] ?: 1f,

                labelSize =
                    preferences[Keys.LABEL_SIZE] ?: 1f,

                showLabels =
                    preferences[Keys.SHOW_LABELS] ?: true,

                showDock =
                    preferences[Keys.SHOW_DOCK] ?: true,

                dockSize =
                    preferences[Keys.DOCK_SIZE] ?: 4,

                hapticFeedback =
                    preferences[Keys.HAPTIC] ?: true,

                reducedMotion =
                    preferences[Keys.REDUCED_MOTION] ?: false
            )
        }

    suspend fun setGrid(
        columns: Int,
        rows: Int
    ) {
        context.launcherDataStore.edit {
            it[Keys.GRID_COLUMNS] = columns.coerceIn(3, 7)
            it[Keys.GRID_ROWS] = rows.coerceIn(4, 10)
        }
    }

    suspend fun setIconSize(value: Float) {
        context.launcherDataStore.edit {
            it[Keys.ICON_SIZE] = value.coerceIn(0.75f, 1.5f)
        }
    }

    suspend fun setLabelSize(value: Float) {
        context.launcherDataStore.edit {
            it[Keys.LABEL_SIZE] = value.coerceIn(0.75f, 1.5f)
        }
    }

    suspend fun setShowLabels(value: Boolean) {
        context.launcherDataStore.edit {
            it[Keys.SHOW_LABELS] = value
        }
    }

    suspend fun setShowDock(value: Boolean) {
        context.launcherDataStore.edit {
            it[Keys.SHOW_DOCK] = value
        }
    }

    suspend fun setDockSize(value: Int) {
        context.launcherDataStore.edit {
            it[Keys.DOCK_SIZE] = value.coerceIn(0, 8)
        }
    }

    suspend fun setHapticFeedback(value: Boolean) {
        context.launcherDataStore.edit {
            it[Keys.HAPTIC] = value
        }
    }

    suspend fun setReducedMotion(value: Boolean) {
        context.launcherDataStore.edit {
            it[Keys.REDUCED_MOTION] = value
        }
    }
}
