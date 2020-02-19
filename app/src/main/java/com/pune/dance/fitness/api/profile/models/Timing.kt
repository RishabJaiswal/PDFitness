package com.pune.dance.fitness.api.profile.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName


@IgnoreExtraProperties
class Timing {

    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = ""


    @get:PropertyName("currency")
    @set:PropertyName("currency")
    var currency: String = ""

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: Int = -1
}