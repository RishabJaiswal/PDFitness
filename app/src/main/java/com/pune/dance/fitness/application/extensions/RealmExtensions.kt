package com.pune.dance.fitness.application.extensions

import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject

fun Realm.completableTransaction(transactionBlock: (realm: Realm) -> Unit): Completable {
    return Completable.create { emitter ->
        this.executeTransactionAsync(
            { realm -> transactionBlock(realm) },
            { emitter.onComplete() },
            { error -> emitter.onError(error) })
    }
}

fun <T : RealmObject> T.asFlowableObject(): Flowable<T> {
    return this.asFlowable<T>()
        .filter { it.isLoaded }
}

fun <T : RealmObject> T.asNonManagedRealmCopy(): T {
    return this.realm.copyFromRealm(this)
}

fun <T : RealmModel> T.asNonManagedRealmCopy(): T {
    return RealmObject.getRealm(this).copyFromRealm(this)
}

