package com.pune.dance.fitness.features.login

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.login.LoginApiManager
import com.pune.dance.fitness.api.login.models.VerificationToken
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.logError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel : ViewModel() {

    lateinit var verificationToken: VerificationToken
    private val loginApiManager by lazy {
        LoginApiManager()
    }

    private val disposable = CompositeDisposable()

    //verifying number
    fun verifyMobileNumber(mobileNo: String) {
        loginApiManager.verifyPhoneNumber(mobileNo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                verificationToken = token
            }, { throwable ->
                logError()
            })
            .addTo(disposable)
    }

    //verifying OTP
    fun verifyOTP(otp: String) {
        loginApiManager.verifyOTP(verificationToken, otp)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(disposable)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}