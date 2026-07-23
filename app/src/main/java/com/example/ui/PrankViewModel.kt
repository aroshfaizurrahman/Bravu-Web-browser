package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.PrankAudioPlayer
import com.example.data.PrankLogEntry
import com.example.data.PrankMode
import com.example.data.PrankRepository
import com.example.data.PrankSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrankViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PrankRepository(application)
    private val audioPlayer = PrankAudioPlayer(application)

    val settings: StateFlow<PrankSettings> = repository.settings
    val logs: StateFlow<List<PrankLogEntry>> = repository.logs
    val prankCount: StateFlow<Int> = repository.prankCount

    private val _inputUrl = MutableStateFlow("bravu://home")
    val inputUrl: StateFlow<String> = _inputUrl.asStateFlow()

    private val _currentDisplayUrl = MutableStateFlow("https://bravu.search")
    val currentDisplayUrl: StateFlow<String> = _currentDisplayUrl.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loadingProgress = MutableStateFlow(0f)
    val loadingProgress: StateFlow<Float> = _loadingProgress.asStateFlow()

    private val _isPrankVisible = MutableStateFlow(false)
    val isPrankVisible: StateFlow<Boolean> = _isPrankVisible.asStateFlow()

    private val _activePrankUrl = MutableStateFlow("")
    val activePrankUrl: StateFlow<String> = _activePrankUrl.asStateFlow()

    private val _isSettingsOpen = MutableStateFlow(false)
    val isSettingsOpen: StateFlow<Boolean> = _isSettingsOpen.asStateFlow()

    private val _isLogsOpen = MutableStateFlow(false)
    val isLogsOpen: StateFlow<Boolean> = _isLogsOpen.asStateFlow()

    private val _isIncognito = MutableStateFlow(false)
    val isIncognito: StateFlow<Boolean> = _isIncognito.asStateFlow()

    private val _tabCount = MutableStateFlow(1)
    val tabCount: StateFlow<Int> = _tabCount.asStateFlow()

    private var navigationJob: Job? = null

    fun onUrlInputChanged(newText: String) {
        _inputUrl.value = newText
    }

    fun openUrlOrSearch(target: String) {
        val formattedTarget = when {
            target.startsWith("http://") || target.startsWith("https://") || target.startsWith("bravu://") -> target
            target.contains(".") && !target.contains(" ") -> "https://$target"
            else -> "https://bravu.search?q=${target.replace(" ", "+")}"
        }

        _inputUrl.value = formattedTarget
        _currentDisplayUrl.value = formattedTarget

        navigationJob?.cancel()
        navigationJob = viewModelScope.launch {
            _isLoading.value = true
            _loadingProgress.value = 0.1f

            val delayStep = (settings.value.fakeLoadingMs / 10).coerceAtLeast(30L)
            for (step in 2..10) {
                delay(delayStep)
                _loadingProgress.value = step / 10f
            }

            _isLoading.value = false
            triggerPrank(formattedTarget)
        }
    }

    private fun triggerPrank(url: String) {
        _activePrankUrl.value = url
        repository.recordPrank(url)

        val currentSettings = settings.value
        if (currentSettings.soundEnabled) {
            if (currentSettings.selectedMode == PrankMode.VIRUS_ALERT) {
                audioPlayer.playAlertAlarm()
            } else {
                audioPlayer.playPrankFanfare()
            }
        }

        if (currentSettings.vibrationEnabled) {
            audioPlayer.triggerVibration()
        }

        _isPrankVisible.value = true
    }

    fun dismissPrank() {
        _isPrankVisible.value = false
    }

    fun toggleIncognito() {
        _isIncognito.value = !_isIncognito.value
    }

    fun addNewTab() {
        _tabCount.value += 1
        _inputUrl.value = "bravu://home"
        _currentDisplayUrl.value = "https://bravu.search"
        _isPrankVisible.value = false
    }

    fun openSettings() {
        _isSettingsOpen.value = true
    }

    fun closeSettings() {
        _isSettingsOpen.value = false
    }

    fun openLogs() {
        _isLogsOpen.value = true
    }

    fun closeLogs() {
        _isLogsOpen.value = false
    }

    fun updateSettings(newSettings: PrankSettings) {
        repository.updateSettings(newSettings)
    }

    fun clearLogs() {
        repository.clearLogs()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
