package com.pune.dance.fitness.api.login

import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class LoginApiManager(val scheduler: Scheduler = AndroidSchedulers.mainThread()) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val phoneAuthProvider = PhoneAuthProvider.getInstance()

    //verify phoneNumber
    fun verifyPhoneNumber(mobileNo: String): Single<String> {
        return Single.create<String> { emitter ->

            phoneAuthProvider.verifyPhoneNumber(mobileNo, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    }

                    override fun onVerificationFailed(excecption: FirebaseException) {
                        emitter.onError(excecption)
                    }

                    override fun onCodeSent(verificationID: String, token: PhoneAuthProvider.ForceResendingToken) {
                        emitter.onSuccess(verificationID)
                    }
                })
        }
            .subscribeOn(scheduler)
    }

    //verify OTP
    fun verifyOTP(verificationID: String, otp: String): Completable {
        return Completable.create { emitter ->

            val credentials = PhoneAuthProvider.getCredential(verificationID, otp)
            firebaseAuth.signInWithCredential(credentials)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //login successful
                        emitter.onComplete()
                    } else {
                        //login failure
                        emitter.onError(Exception("OTP login failure"))
                    }
                }
        }
            .subscribeOn(scheduler)
    }
}