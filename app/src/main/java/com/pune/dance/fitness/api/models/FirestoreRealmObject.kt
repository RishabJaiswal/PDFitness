package com.pune.dance.fitness.api.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import io.realm.RealmObject

@IgnoreExtraProperties
open class FirestoreRealmObject : RealmObject() {
    @Exclude
    var loaded: Boolean = super.isLoaded()

    @Exclude
    var maanged: Boolean = super.isManaged()

    @Exclude
    var valid: Boolean = super.isValid()
}