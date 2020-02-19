package com.pune.dance.fitness.data

import com.pune.dance.fitness.application.extensions.completableTransaction
import io.reactivex.Completable
import io.realm.Realm
import io.realm.RealmModel
import java.io.Closeable

open class BaseDao(open val realm: Realm = Realm.getDefaultInstance()) : Closeable {

    fun save(realmModel: RealmModel) {
        realm.copyToRealmOrUpdate(realmModel)
    }

    fun saveAsync(realmModel: RealmModel): Completable {
        return realm.completableTransaction { bgRealm ->
            bgRealm.copyToRealmOrUpdate(realmModel)
        }
    }

    override fun close() {
        realm.close()
    }
}