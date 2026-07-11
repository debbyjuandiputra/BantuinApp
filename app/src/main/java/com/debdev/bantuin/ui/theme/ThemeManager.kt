package com.debdev.bantuin.ui.theme

import android.content.Context
import android.content.SharedPreferences

/**
 * Menyimpan preferensi mode terang/gelap secara lokal di device.
 * Default aplikasi SELALU mode terang saat pertama kali dibuka.
 */
class ThemeManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("bantuin_theme_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DARK_MODE = "is_dark_mode"
    }

    fun isDarkMode(): Boolean {
        // default false = mode terang
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun setDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply()
    }

    fun toggle(): Boolean {
        val newValue = !isDarkMode()
        setDarkMode(newValue)
        return newValue
    }
}
