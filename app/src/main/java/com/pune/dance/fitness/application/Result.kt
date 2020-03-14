package com.pune.dance.fitness.application

sealed class Result<T> {

    class Progress<T> : Result<T>()
    data class Success<T>(var data: T) : Result<T>()
    data class Error<T>(var error: Throwable) : Result<T>()

    companion object {
        fun <T> loading(): Result<T> = Progress()
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> error(error: Throwable): Result<T> = Error(error)
    }

    fun isLoading() = this is Progress
    fun isError() = this is Error
    fun isSuccess() = this is Success

    fun getResult(): T? {
        return if (this is Result.Success) {
            return this.data

        } else null
    }

    fun parseResult(
        progress: () -> Unit,
        success: (data: T) -> Unit,
        error: (error: Throwable) -> Unit
    ) {
        when (this) {
            // loading
            is Progress -> {
                progress()
            }

            //success
            is Success -> {
                success(this.data)
            }

            //error
            is Error -> {
                error(this.error)
            }
        }
    }
}