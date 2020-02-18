package com.pune.dance.fitness.api.user.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {

    //UID of Firebase user
    @PrimaryKey
    var id: String = ""

}