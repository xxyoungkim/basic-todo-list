package com.young.mytodo.ui.settings.util

import android.content.Context
import androidx.core.content.edit

class ThemePreferences(private val context: Context) {
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun getThemeMode(): ThemeMode {
        val mode = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        return ThemeMode.valueOf(mode ?: ThemeMode.SYSTEM.name)
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit { putString("theme_mode", mode.name) }
//        prefs.edit().putString("theme_mode", mode.name).apply()
    }
}