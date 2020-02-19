package com.pune.dance.fitness.api.user.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@IgnoreExtraProperties
@RealmClass
open class UserProfile : RealmModel {

    @get:PropertyName("name")
    @set:PropertyName("name")
    public var displayName: String = "Ri"

    @get:PropertyName("mobile_name")
    @set:PropertyName("mobile_name")
    var mobileNo: String = "3"
}