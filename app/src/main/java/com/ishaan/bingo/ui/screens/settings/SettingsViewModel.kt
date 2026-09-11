package com.ishaan.bingo.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.ishaan.bingo.data.repository.SettingsPreferences
import com.ishaan.bingo.ui.domain.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

class SettingsViewModel(
    private val preferences: SettingsPreferences? = null
) : ViewModel() {
    private val _themeMode = MutableStateFlow(preferences?.themeMode ?: ThemeMode.SYSTEM)
    val themeMode = _themeMode.asStateFlow()

    private val _fontScale = MutableStateFlow(preferences?.fontScale ?: 0.8f)
    val fontScale = _fontScale.asStateFlow()

    private val _confirmCalls = MutableStateFlow(preferences?.confirmCalls ?: false)
    val confirmCalls = _confirmCalls.asStateFlow()

    private val _hapticsEnabled = MutableStateFlow(preferences?.hapticsEnabled ?: false)
    val hapticsEnabled = _hapticsEnabled.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        preferences?.themeMode = mode
    }

    fun setFontScale(scale: Float) {
        val rounded = (scale * 10f).roundToInt() / 10f
        _fontScale.value = rounded
        preferences?.fontScale = rounded
    }

    fun setConfirmCalls(enabled: Boolean) {
        _confirmCalls.value = enabled
        preferences?.confirmCalls = enabled
    }

    fun setHapticsEnabled(enabled: Boolean) {
        _hapticsEnabled.value = enabled
        preferences?.hapticsEnabled = enabled
    }
}
