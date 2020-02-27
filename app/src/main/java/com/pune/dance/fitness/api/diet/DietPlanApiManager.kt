package com.pune.dance.fitness.api.diet

import com.google.firebase.firestore.FirebaseFirestore
import com.pune.dance.fitness.api.diet.models.DietPlan
import com.pune.dance.fitness.application.exceptions.ContentNotFoundException
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class DietPlanApiManager {

    private val firestoreDb = FirebaseFirestore.getInstance()

    fun getDietPlan(dietPlanId: String): Flowable<DietPlan> {
        return Flowable.create<DietPlan>({ emitter ->

            //accessing diet plan
            firestoreDb.collection("diet_plans")
                .document(dietPlanId)
                .addSnapshotListener { snapshot, exception ->

                    //error
                    if (exception != null) {
                        emitter.onError(exception)
                    }

                    //success
                    if (snapshot != null && snapshot.exists()) {
                        val dietPlan = snapshot.toObject(DietPlan::class.java)
                        if (dietPlan != null) {
                            emitter.onNext(dietPlan)
                        } else {
                            emitter.onError(ContentNotFoundException())
                        }
                    } else {
                        emitter.onError(ContentNotFoundException())
                    }
                }

        }, BackpressureStrategy.BUFFER)
    }
}