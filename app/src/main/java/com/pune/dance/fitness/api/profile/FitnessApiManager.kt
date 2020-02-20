package com.pune.dance.fitness.api.profile

import com.google.firebase.firestore.FirebaseFirestore
import com.pune.dance.fitness.api.profile.models.FitnessSession
import io.reactivex.Single

class FitnessApiManager {

    private val firestoreDB = FirebaseFirestore.getInstance()

    fun getFitnessSessions(): Single<List<FitnessSession>> {
        return Single.create { emitter ->

            firestoreDB.collection("fitness_sessions")
                .get()
                .addOnSuccessListener { result ->
                    emitter.onSuccess(
                        if (result.isEmpty) {
                            emptyList<FitnessSession>()
                        } else {
                            result.toObjects(FitnessSession::class.java)
                        }
                    )
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }
    }
}