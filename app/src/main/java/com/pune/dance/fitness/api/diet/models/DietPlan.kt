package com.pune.dance.fitness.api.diet.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@IgnoreExtraProperties
@RealmClass
open class DietPlan : RealmModel {

    @get:PropertyName("meals")
    @set:PropertyName("meals")
    var meal: List<Meal>? = null
}