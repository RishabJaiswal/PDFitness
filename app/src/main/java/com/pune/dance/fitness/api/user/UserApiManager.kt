package com.pune.dance.fitness.api.user

import com.google.firebase.auth.FirebaseAuth
import com.pune.dance.fitness.api.user.models.User
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class UserApiManager(val scheduler: Scheduler = AndroidSchedulers.mainThread()) {

    private val firebaseAuth = FirebaseAuth.getInstance()

    //getting current PDF user
    fun getCurrentUser(): Single<User> {
        return Single.create<User> { emitter ->

            //creating PDF user from Firebase user
            firebaseAuth.currentUser?.let { firebaseUser ->
                emitter.onSuccess(User().apply {
                    id = firebaseUser.uid
                })
            } ?: emitter.onError(NullPointerException("Firebase user not found"))
        }
            .subscribeOn(scheduler)
    }
}