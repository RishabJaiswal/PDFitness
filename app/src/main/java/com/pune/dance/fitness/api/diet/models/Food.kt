package com.pune.dance.fitness.api.diet.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Food(

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = ""
)