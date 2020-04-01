package com.pune.dance.fitness.ui.home.models

import java.util.*

data class CalendarItem(val date: Date) {
    var status: AttendanceStatus = AttendanceStatus.UNKNOWN
}