package com.pune.dance.fitness.api.login

import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pune.dance.fitness.api.login.models.VerificationToken
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class LoginApiManager(val scheduler: Scheduler = AndroidSchedulers.mainThread()) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val phoneAuthProvider = PhoneAuthProvider.getInstance()

    //verify phoneNumber
    fun verifyPhoneNumber(mobileNo: String): Single<VerificationToken> {
        return Single.create<VerificationToken> { emitter ->

            //verification
            val verificationToken = VerificationToken()

            phoneAuthProvider.verifyPhoneNumber(mobileNo, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        verificationToken.authCredential = phoneAuthCredential
                        emitter.onSuccess(verificationToken)
                    }

                    override fun onVerificationFailed(excecption: FirebaseException) {
                        emitter.onError(excecption)
                    }

                    override fun onCodeSent(verificationID: String, token: PhoneAuthProvider.ForceResendingToken) {
                        verificationToken.verificationID = verificationID
                        verificationToken.resendingToken = token
                        emitter.onSuccess(verificationToken)
                    }
                })
        }
            .subscribeOn(scheduler)
    }

    //verify OTP
    fun verifyOTP(verificationToken: VerificationToken, otp: String): Completable {
        return Completable.create { emitter ->

            //checkin for a valid authCredential
            if (verificationToken.authCredential == null) {
                verificationToken.authCredential =
                    PhoneAuthProvider.getCredential(verificationToken.verificationID, otp)
            }

            //signing in with auth credential
            firebaseAuth.signInWithCredential(verificationToken.authCredential!!)
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