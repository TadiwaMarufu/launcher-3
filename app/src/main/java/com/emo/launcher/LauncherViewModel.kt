package com.emo.launcher

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emo.launcher.data.AppRepository
import com.emo.launcher.data.LauncherPreferences
import com.emo.launcher.model.AppInfo
import com.emo.launcher.model.LauncherSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LauncherViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AppRepository(application)

    private val preferences =
        LauncherPreferences(application)

    private val _apps =
        MutableStateFlow<List<AppInfo>>(emptyList())

    val apps: StateFlow<List<AppInfo>> =
        _apps.asStateFlow()

    private val _query =
        MutableStateFlow("")

    val query: StateFlow<String> =
        _query.asStateFlow()

    private val _drawerOpen =
        MutableStateFlow(false)

    val drawerOpen: StateFlow<Boolean> =
        _drawerOpen.asStateFlow()

    private val _settings =
        MutableStateFlow(LauncherSettings())

    val settings: StateFlow<LauncherSettings> =
        _settings.asStateFlow()

    init {
        refreshApps()

        viewModelScope.launch {
            preferences.settings.collectLatest {
                _settings.value = it
            }
        }
    }

    fun refreshApps() {
        viewModelScope.launch {
            _apps.value =
                repository.loadApps()
        }
    }

    fun setQuery(value: String) {
        _query.value = value
    }

    fun openDrawer() {
        _drawerOpen.value = true
        _query.value = ""
    }

    fun closeDrawer() {
        _drawerOpen.value = false
        _query.value = ""
    }

    fun filteredApps(): List<AppInfo> {
        val q = _query.value
            .trim()
            .lowercase()

        if (q.isEmpty()) {
            return _apps.value
        }

        return _apps.value
            .map { app ->
                val label =
                    app.label.lowercase()

                val packageName =
                    app.packageName.lowercase()

                val score = when {
                    label == q -> 0
                    label.startsWith(q) -> 1
                    label.contains(q) -> 2
                    packageName.contains(q) -> 3
                    else -> 99
                }

                app to score
            }
            .filter {
                it.second < 99
            }
            .sortedWith(
                compareBy(
                    { it.second },
                    { it.first.label.lowercase() }
                )
            )
            .map {
                it.first
            }
    }

    fun launch(app: AppInfo) {
        val context =
            getApplication<Application>()

        val intent = Intent().apply {
            component = ComponentName(
                app.packageName,
                app.activityName
            )

            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        }

        runCatching {
            context.startActivity(intent)
        }
    }

    fun updateGrid(
        columns: Int,
        rows: Int
    ) {
        viewModelScope.launch {
            preferences.setGrid(
                columns,
                rows
            )
        }
    }

    fun updateIconSize(
        value: Float
    ) {
        viewModelScope.launch {
            preferences.setIconSize(value)
        }
    }

    fun updateLabelSize(
        value: Float
    ) {
        viewModelScope.launch {
            preferences.setLabelSize(value)
        }
    }

    fun updateShowLabels(
        value: Boolean
    ) {
        viewModelScope.launch {
            preferences.setShowLabels(value)
        }
    }

    fun updateShowDock(
        value: Boolean
    ) {
        viewModelScope.launch {
            preferences.setShowDock(value)
        }
    }

    fun updateHapticFeedback(
        value: Boolean
    ) {
        viewModelScope.launch {
            preferences.setHapticFeedback(value)
        }
    }

    fun updateReducedMotion(
        value: Boolean
    ) {
        viewModelScope.launch {
            preferences.setReducedMotion(value)
        }
    }
}
