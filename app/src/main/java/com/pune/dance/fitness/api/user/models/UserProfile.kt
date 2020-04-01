package com.pune.dance.fitness.api.user.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@IgnoreExtraProperties
@RealmClass
open class UserProfile : RealmModel {

    @get:Exclude
    @PropertyName("user_id")
    @PrimaryKey
    var userId: String = ""

    @get:Exclude
    @PropertyName("name")
    var displayName: String = ""

    @get:Exclude
    @PropertyName("mobile_number")
    var mobileNo: String = ""

    @get:Exclude
    @PropertyName("fitness_session_id")
    var fitness_session_id: String = ""

    @get:Exclude
    @PropertyName("session_timing_id")
    var session_timing_id: String = ""

    @get:Exclude
    @PropertyName("diet_plan_id")
    var dietPlanId: String = ""

}