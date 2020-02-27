package com.pune.dance.fitness.api.diet.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import io.realm.RealmList

@IgnoreExtraProperties
class Meal {

    @get:PropertyName("meal_name")
    @set:PropertyName("meal_name")
    var mealName: String = ""


    @get:PropertyName("food_items")
    var foodItems: RealmList<Food> = RealmList()

    @PropertyName("food_items")
    fun setFoodItemsList(foodItemsList: ArrayList<Food>) {
        foodItems.addAll(foodItemsList)
    }

}