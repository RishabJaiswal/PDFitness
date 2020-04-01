package com.pune.dance.fitness.ui.login

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
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
import java.util.concurrent.TimeUnit

class LoginViewModel : ViewModel() {

    private val loginApiManager by lazy { LoginApiManager() }
    private val userApiManager by lazy { UserApiManager() }
    private val disposable = CompositeDisposable()

    val verificationTokenLiveResult = LiveResult<VerificationToken>()
    val otpTimerMillisLeft: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val loginLiveResult = LiveResult<Boolean>()

    private var verifiedMobileNo: String = ""
    private val userDao = UserDao()
    private var timer: CountDownTimer? = null
    private val OTP_TIMER_MILLIS = 120000L
    private val OTP_TIMER_INTERVAL = 1000L
    private val CC_INIDA = "+91"

    //verifying number
    fun verifyMobileNumber(userInputMobileNo: String) {

        var mobileNo = userInputMobileNo
        if (userInputMobileNo.startsWith("+91").not()) {
            mobileNo = CC_INIDA + userInputMobileNo
        }

        /**this is a VerificationToken which has a ForceResendingToken*/
        var resendVerificationToken: VerificationToken? = null
        if (mobileNo == verifiedMobileNo) {
            resendVerificationToken = verificationTokenLiveResult.getResult()
        }

        //verifying number and requesting | resending an OTP
        loginApiManager.verifyPhoneNumber(mobileNo, OTP_TIMER_MILLIS, TimeUnit.MILLISECONDS, resendVerificationToken)
            .doOnSubscribe { verificationTokenLiveResult.loading() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                this.verifiedMobileNo = mobileNo
                verificationTokenLiveResult.success(token)
                startTimer()
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
                //creating fresh/new user profile
                val profile = UserProfile().apply {
                    userId = user.id
                    this.mobileNo = this@LoginViewModel.verifiedMobileNo
                }
                userApiManager.setUserProfile(user.id, profile)
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

    /**timer represents the time until the incoming OTP is valid*/
    private fun startTimer() {
        timer = object : CountDownTimer(OTP_TIMER_MILLIS, OTP_TIMER_INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                otpTimerMillisLeft.value = millisUntilFinished
            }

            override fun onFinish() {
                otpTimerMillisLeft.value = 0
            }
        }
        timer?.start()
    }

    fun stopTimer() {
        timer?.cancel()
    }

    fun getOtpTimeLeft() = otpTimerMillisLeft.value ?: -1

    override fun onCleared() {
        disposable.dispose()
        userDao.close()
        stopTimer()
        super.onCleared()
    }
}