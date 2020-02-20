package com.pune.dance.fitness.api.profile.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName


@IgnoreExtraProperties
data class FitnessSessionTiming(

    @get:PropertyName("_id")
    @set:PropertyName("_id")
    var id: String = "",


    @get:PropertyName("currency")
    @set:PropertyName("currency")
    var currency: String = "",

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: Int = -1,

    @get:PropertyName("time")
    @set:PropertyName("time")
    var time: Timestamp = Timestamp.now()
)