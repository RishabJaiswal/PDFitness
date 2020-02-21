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

    fun isLoading() = this.value is Result.Progress

    fun getResult(): T? {
        val resultValue = this.value
        return if (resultValue is Result.Success) {
            return resultValue.data

        } else null
    }

}