package com.ishaan.bingo.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.ishaan.bingo.ui.domain.model.ThemeMode

class SettingsPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("bingo_settings", Context.MODE_PRIVATE)

    var themeMode: ThemeMode
        get() {
            val name = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
            return try {
                ThemeMode.valueOf(name ?: ThemeMode.SYSTEM.name)
            } catch (e: Exception) {
                ThemeMode.SYSTEM
            }
        }
        set(value) {
            prefs.edit().putString(KEY_THEME_MODE, value.name).apply()
        }

    var fontScale: Float
        get() = prefs.getFloat(KEY_FONT_SCALE, 0.8f)
        set(value) {
            prefs.edit().putFloat(KEY_FONT_SCALE, value).apply()
        }

    var confirmCalls: Boolean
        get() = prefs.getBoolean(KEY_CONFIRM_CALLS, false)
        set(value) {
            prefs.edit().putBoolean(KEY_CONFIRM_CALLS, value).apply()
        }

    var hapticsEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTICS_ENABLED, false)
        set(value) {
            prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, value).apply()
        }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_FONT_SCALE = "font_scale"
        private const val KEY_CONFIRM_CALLS = "confirm_calls"
        private const val KEY_HAPTICS_ENABLED = "haptics_enabled"
    }
}
