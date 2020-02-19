package com.pune.dance.fitness.application.extensions

import io.reactivex.Completable
import io.realm.Realm

fun Realm.completableTransaction(transactionBlock: (realm: Realm) -> Unit): Completable {
    return Completable.create { emitter ->
        this.executeTransactionAsync(
            { realm -> transactionBlock(realm) },
            { emitter.onComplete() },
            { error -> emitter.onError(error) })
    }
}

