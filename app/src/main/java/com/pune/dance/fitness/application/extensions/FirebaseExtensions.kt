package com.pune.dance.fitness.application.extensions

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.pune.dance.fitness.application.exceptions.ContentNotFoundException
import java.text.SimpleDateFormat

fun Timestamp.toString(dateFormat: String): String {
    val date = this.toDate()
    val sdf = SimpleDateFormat(dateFormat)
    return sdf.format(date)
}

inline fun <reified T> DocumentSnapshot?.parseToModel(
    exception: Exception?,
    onSuccess: (modelObject: T) -> Unit,
    onError: (error: Exception) -> Unit
) {
    //error
    if (exception != null) {
        onError(exception)
    }

    //success
    try {
        if (this != null && this.exists()) {
            val modelObject = this.toObject(T::class.java)
            if (modelObject != null) {
                onSuccess(modelObject)
            } else {
                onError(ContentNotFoundException())
            }
        } else {
            onError(ContentNotFoundException())
        }
    } catch (error: Exception) {
        onError(error)
    }
}

inline fun <reified T> QuerySnapshot?.parseToModelsList(
    exception: Exception?,
    onSuccess: (modelObjectsList: List<T>) -> Unit,
    onError: (error: Exception) -> Unit
) {
    //error
    if (exception != null) {
        onError(exception)
    }

    //success
    try {
        if (this != null) {
            val modelObjectsList = this.toObjects(T::class.java)
            onSuccess(modelObjectsList)
        } else {
            onError(ContentNotFoundException())
        }
    } catch (error: Exception) {
        onError(error)
    }
}