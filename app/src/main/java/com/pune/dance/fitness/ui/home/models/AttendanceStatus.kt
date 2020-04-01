package com.pune.dance.fitness.ui.home.models

enum class AttendanceStatus(val value: String) {
    PRESENT("present"),
    ABSENT("absent"),
    UNKNOWN("unknown");

    companion object {
        private val values = values().associateBy(AttendanceStatus::value)
        fun from(value: String): AttendanceStatus = values[value] ?: UNKNOWN
    }
}