package com.pune.dance.fitness.data

import com.google.firebase.firestore.FirebaseFirestore
import com.pune.dance.fitness.api.user.models.User
import com.pune.dance.fitness.api.user.models.UserFields
import io.realm.Realm
import java.io.Closeable

class UserDao(val db: Realm = Realm.getDefaultInstance()) : Closeable {

    private val firestoreDB = FirebaseFirestore.getInstance()

    fun getUser(userId: String): User? {
        return db.where(User::class.java)
            .equalTo(UserFields.ID, userId)
            .findFirst()
    }

    fun saveUser(user: User) {
        db.executeTransaction { realm ->
            realm.copyToRealmOrUpdate(user)
        }
    }

    override fun close() {
        db.close()
    }
}