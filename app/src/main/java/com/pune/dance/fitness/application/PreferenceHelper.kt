package com.pune.dance.fitness.application

import android.content.Context
import android.content.SharedPreferences
import com.pune.dance.fitness.application.extensions.logError

object PreferenceHelper {

    private var preference: SharedPreferences? = null

    private fun getPreference(): SharedPreferences {
        if (preference == null) {
            preference = PdfApplication.instance
                .getSharedPreferences("pdf_app_preferences", Context.MODE_PRIVATE)
        }
        return preference!!
    }

    fun putValue(key: String, value: Any?) {
        getPreference().edit().run {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                else -> logError(message = "Attempting to save non-primitive value in preference for $key")
            }
            apply()
        }
    }

    fun getString(key: String): String = getPreference().getString(key, "") ?: ""

    fun getInt(key: String): Int = getPreference().getInt(key, 0)

    fun getFloat(key: String): Float = getPreference().getFloat(key, 0F)

    fun getLong(key: String): Long = getPreference().getLong(key, 0)

    fun getBoolean(key: String): Boolean = getPreference().getBoolean(key, false)

    fun existInPreferences(key: String): Boolean {
        return getPreference().contains(key)
    }

    fun delete(key: String) {
        if (getPreference().contains(key)) {
            getPreference().edit().remove(key).apply()
        } else {
            logError(message = "$key does not exist in preferences")
        }
    }

    /**
     *  reified means variable needs to declare type explicitly while using this getter
     *  eg. val count: Int = getValue(key)
     */
    inline fun <reified T> getValue(key: String): T {
        return when (T::class) {
            Int::class -> getInt(key) as T
            Float::class -> getFloat(key) as T
            Long::class -> getLong(key) as T
            String::class -> getString(key) as T
            Boolean::class -> getBoolean(key) as T
            else -> throw IllegalStateException("Preference type not supported")
        }
    }

    fun clearPreferences() = getPreference().edit().clear().apply()
}