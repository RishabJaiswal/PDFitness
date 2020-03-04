package com.pune.dance.fitness.api.attendance.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

class Attendance {

    @get:PropertyName("session_id")
    @set:PropertyName("session_id")
    var sessionId: String = ""

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = ""

    @get:PropertyName("status")
    @set:PropertyName("status")
    var status: String = ""

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Timestamp = Timestamp.now()
}