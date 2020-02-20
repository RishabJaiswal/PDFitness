package com.pune.dance.fitness.application.extensions

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

fun Timestamp.toString(dateFormat: String): String {
    val date = this.toDate()
    val sdf = SimpleDateFormat(dateFormat)
    return sdf.format(date)
}