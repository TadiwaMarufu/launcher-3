package com.emo.launcher

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

class LauncherViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AppRepository(application)

    private val _apps =
        MutableStateFlow<List<AppInfo>>(emptyList())

    val apps: StateFlow<List<AppInfo>> =
        _apps.asStateFlow()

    init {
        refreshApps()
    }

    fun refreshApps() {

        viewModelScope.launch {

            _apps.value =
                repository.loadApps()
        }
    }

    fun launch(
        app: AppInfo
    ) {

        val context =
            getApplication<Application>()

        val intent =
            Intent().apply {

                component =
                    ComponentName(
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
}