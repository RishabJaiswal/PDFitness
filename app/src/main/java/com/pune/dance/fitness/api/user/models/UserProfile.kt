package com.pune.dance.fitness.api.user.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@IgnoreExtraProperties
open class UserProfile : RealmObject() {

    @PrimaryKey
    @PropertyName("name")
    var name: String = "Rishab"

    @PropertyName("phone")
    var phone: String = "9389863860"

}