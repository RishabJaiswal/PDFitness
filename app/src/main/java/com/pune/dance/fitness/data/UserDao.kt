package com.pune.dance.fitness.data

import com.google.firebase.firestore.FirebaseFirestore
import com.pune.dance.fitness.api.user.models.User
import io.reactivex.Single
import java.io.Closeable

class UserDao : Closeable {

    private val firestoreDB = FirebaseFirestore.getInstance()

    fun getUser(userId: String): Single<User?> {
        return Single.create { emitter ->
            firestoreDB.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    emitter.onSuccess(user!!)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun saveUser() {

    }

    override fun close() {
    }
}