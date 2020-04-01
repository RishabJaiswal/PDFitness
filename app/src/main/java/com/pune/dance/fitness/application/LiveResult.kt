package com.pune.dance.fitness.application

import androidx.lifecycle.MutableLiveData

class LiveResult<T> : MutableLiveData<Result<T>>() {

    fun loading() {
        value = Result.loading()
    }

    fun success(data: T) {
        value = Result.success(data)
    }

    /**this used when setting value from background thread*/
    fun postSucess(data: T) {
        postValue(Result.success(data))
    }

    fun error(error: Throwable) {
        value = Result.error(error)
    }

    fun isLoading() = this.value?.isLoading() ?: false
    fun isSuccess() = this.value?.isSuccess() ?: false
    fun isError() = this.value?.isError() ?: false
    fun getResult() = this.value?.getResult()

}