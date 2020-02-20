package com.pune.dance.fitness.api.user.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@IgnoreExtraProperties
@RealmClass
open class UserProfile : RealmModel {

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    @PrimaryKey
    var userId: String = ""

    @get:PropertyName("name")
    @set:PropertyName("name")
    var displayName: String = ""

    @get:PropertyName("mobile_number")
    @set:PropertyName("mobile_number")
    var mobileNo: String = ""
}