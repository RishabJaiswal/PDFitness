package com.pune.dance.fitness.api.diet.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@IgnoreExtraProperties
@RealmClass
open class Meal : RealmModel {

    @get:PropertyName("meal_name")
    @set:PropertyName("meal_name")
    var mealName: String = ""


    @get:PropertyName("food_items")
    @set:PropertyName("food_items")
    var foodItems: List<Food> = emptyList()

}