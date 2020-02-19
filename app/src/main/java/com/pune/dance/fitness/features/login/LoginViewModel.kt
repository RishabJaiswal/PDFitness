package com.pune.dance.fitness.features.login

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.login.LoginApiManager
import com.pune.dance.fitness.api.login.models.VerificationToken
import com.pune.dance.fitness.api.user.UserApiManager
import com.pune.dance.fitness.api.user.models.UserProfile
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.flatMapResumeAfter
import com.pune.dance.fitness.application.extensions.logError
import com.pune.dance.fitness.application.extensions.subscribeOnBackObserverOnMain
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
    private val userApiManager by lazy {
        UserApiManager()
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
        verificationTokenLiveResult.getResult()?.let { token ->

            loginApiManager.verifyOTP(token, otp)
                .doOnSubscribe { loginLiveResult.loading() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getUser()
                }, {
                    logError(throwable = it)
                    loginLiveResult.error(it)
                })
                .addTo(disposable)
        } ?: loginLiveResult.error(NullPointerException())
    }


    private fun getUser() {
        userApiManager.getCurrentUser()
            .map { user ->
                UserDao().use { it.save(user) }
                return@map user
            }
            .flatMap { user ->
                userApiManager.getUserProfile(user.id)
            }
            .subscribeOnBackObserverOnMain()
            .subscribe({ userProfile ->
                //save user profile
                userDao.save(userProfile)
                loginLiveResult.success(true)
            }, { error ->
                if (error is NullPointerException) {
                    createUserProfile()
                }
            })
            .addTo(disposable)
    }

    /**create user profile when the user is new*/
    private fun createUserProfile() {
        userApiManager.getCurrentUser()
            .flatMap { user ->
                val profile = UserProfile().apply { userId = user.id }
                userApiManager.createUserProfile(user.id, profile)
            }
            .subscribeOnBackObserverOnMain()
            .flatMapResumeAfter {
                //saving user profile
                userDao.saveAsync(it)
            }
            .subscribe({
                loginLiveResult.success(true)
            }, { error ->
                loginLiveResult.error(error)
            })
            .addTo(disposable)
    }

    override fun onCleared() {
        disposable.dispose()
        userDao.close()
        super.onCleared()
    }
}