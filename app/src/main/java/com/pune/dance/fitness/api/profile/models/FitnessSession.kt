package com.pune.dance.fitness.api.profile.models

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName

data class FitnessSession(

    @get:PropertyName("display_address")
    @set:PropertyName("display_address")
    var displayAddress: String = "",


    @get:PropertyName("location")
    @set:PropertyName("location")
    var location: GeoPoint? = null,

    @get:PropertyName("timings")
    @set:PropertyName("timings")
    var timings: List<Timing> = emptyList()
)