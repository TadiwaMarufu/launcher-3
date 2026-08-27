package com.emo.launcher.ui

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emo.launcher.data.AppRepository
import com.emo.launcher.model.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _drawerOpen = MutableStateFlow(false)
    val drawerOpen: StateFlow<Boolean> = _drawerOpen.asStateFlow()

    init {
        refreshApps()
    }

    fun refreshApps() {
        viewModelScope.launch {
            _apps.value = repository.loadApps()
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
        val q = _query.value.trim()
        if (q.isEmpty()) return _apps.value

        return _apps.value
            .map { app ->
                val label = app.label.lowercase()
                val pkg = app.packageName.lowercase()
                val query = q.lowercase()
                val score = when {
                    label == query -> 0
                    label.startsWith(query) -> 1
                    label.contains(query) -> 2
                    pkg.contains(query) -> 3
                    else -> 99
                }
                app to score
            }
            .filter { it.second < 99 }
            .sortedWith(compareBy({ it.second }, { it.first.label.lowercase() }))
            .map { it.first }
    }

    fun launch(app: AppInfo) {
        val context = getApplication<Application>()
        val intent = Intent().apply {
            component = ComponentName(app.packageName, app.activityName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }
    }
}
