package com.pune.dance.fitness.features.login

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.login.LoginApiManager
import com.pune.dance.fitness.api.login.models.VerificationToken
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.Result
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.logError
import com.pune.dance.fitness.data.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel : ViewModel() {

    private val userDao = UserDao()
    val verificationTokenLiveResult = LiveResult<VerificationToken>()
    val loginLiveResult = LiveResult<Boolean>()

    private val loginApiManager by lazy {
        LoginApiManager()
    }

    private val disposable = CompositeDisposable()

    //verifying number
    fun verifyMobileNumber(mobileNo: String) {
        loginApiManager.verifyPhoneNumber(mobileNo)
            .doOnSubscribe { verificationTokenLiveResult.loading() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                verificationTokenLiveResult.success(token)
            }, {
                logError(throwable = it)
                verificationTokenLiveResult.error(it)
            })
            .addTo(disposable)
    }

    //verifying OTP
    fun verifyOTP(otp: String) {
        if (verificationTokenLiveResult.value is Result.Success) {
            (verificationTokenLiveResult.value as Result.Success<VerificationToken>).data.let { token ->

                loginApiManager.verifyOTP(token, otp)
                    .doOnSubscribe { loginLiveResult.loading() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        loginLiveResult.success(true)
                    }, {
                        logError(throwable = it)
                        loginLiveResult.error(it)
                    })
                    .addTo(disposable)
            }
        }
    }

    override fun onCleared() {
        disposable.dispose()
        userDao.close()
        super.onCleared()
    }
}