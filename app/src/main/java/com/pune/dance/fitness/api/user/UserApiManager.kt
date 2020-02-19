package com.pune.dance.fitness.api.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pune.dance.fitness.api.user.models.User
import com.pune.dance.fitness.api.user.models.UserProfile
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class UserApiManager(val scheduler: Scheduler = AndroidSchedulers.mainThread()) {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestoreDB by lazy {
        FirebaseFirestore.getInstance()
    }

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
    }

    //saving user profile
    fun getUserProfile(userId: String): Single<UserProfile> {
        return Single.create<UserProfile> { emitter ->
            firestoreDB.document("user_profile/${userId}")
                .get()
                .addOnSuccessListener { document ->
                    //success
                    document.toObject(UserProfile::class.java)?.let { userProfile ->
                        emitter.onSuccess(userProfile)
                    } ?: emitter.onError(NullPointerException())
                }
                .addOnFailureListener {
                    //error
                    emitter.onError(it)
                }
        }
    }

    fun createUserProfile(userId: String, profile: UserProfile): Single<UserProfile> {
        return Single.create<UserProfile> { emitter ->
            firestoreDB.document("user_profile/${userId}")
                .set(profile)
                .addOnSuccessListener {
                    emitter.onSuccess(profile)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}