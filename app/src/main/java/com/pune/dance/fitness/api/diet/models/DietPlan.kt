package com.pune.dance.fitness.api.diet.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
class DietPlan {

    @get:PropertyName("meals")
    @set:PropertyName("meals")
    var meals: List<Meal> = emptyList()
}