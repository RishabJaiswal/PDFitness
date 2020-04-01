package com.pune.dance.fitness.application.extensions

import java.util.*

fun Date.stripTime(): Date {
    val inputTime = this
    return Calendar.getInstance().apply {
        time = inputTime
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}