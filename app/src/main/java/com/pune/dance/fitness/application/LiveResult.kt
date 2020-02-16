package com.pune.dance.fitness.application

import androidx.lifecycle.MutableLiveData

class LiveResult<T> : MutableLiveData<Result<T>>() {

    fun loading() {
        value = Result.loading()
    }

    fun success(data: T) {
        value = Result.success(data)
    }

    fun error(error: Throwable) {
        value = Result.error(error)
    }

    fun isSuccess() = this.value is Result.Success

}