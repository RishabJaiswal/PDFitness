package com.pune.dance.fitness.api.attendance

import com.google.firebase.firestore.FirebaseFirestore
import com.pune.dance.fitness.api.attendance.models.Attendance
import com.pune.dance.fitness.application.extensions.parseToModelsList
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class AttendanceApiManager(val scheduler: Scheduler = AndroidSchedulers.mainThread()) {

    private val firestoreDb = FirebaseFirestore.getInstance()

    fun getAttendance(userId: String, sessionId: String, startDate: Date, endDate: Date): Flowable<List<Attendance>> {
        return Flowable.create<List<Attendance>>({ emitter ->
            firestoreDb
                .collection("session_attendance")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("session_id", sessionId)
                /*.whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate)*/
                .addSnapshotListener { snapshot, firebaseFirestoreException ->
                    snapshot.parseToModelsList<Attendance>(firebaseFirestoreException,
                        { attendanceList ->
                            emitter.onNext(attendanceList)
                        }, { error ->
                            emitter.onError(error)
                        })
                }
        }, BackpressureStrategy.LATEST)
    }

    fun markAttendance(attendance: Attendance): Completable {
        return Completable.create { emitter ->

            //creating attendance document
            val document = firestoreDb.collection("session_attendance")
                .let { collectionRef ->
                    if (attendance.id.isEmpty()) {
                        collectionRef.document().apply {
                            attendance.id = this.id
                        }
                    } else {
                        collectionRef.document(attendance.id)
                    }
                }

            //setting
            document
                .set(attendance)
                .addOnSuccessListener { _docReference ->
                    emitter.onComplete()
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }
    }
}