package com.pune.dance.fitness.features.login

import android.os.Bundle
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pune.dance.fitness.BuildConfig
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.gone
import com.pune.dance.fitness.application.extensions.visible
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity(), View.OnClickListener {

    var verificationID = ""
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener(this)
    }

    fun verifyPhoneNumber(mobileNo: String) {
        showProgress()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobileNo,
            80,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    hideProgress()
                }

                override fun onVerificationFailed(excecption: FirebaseException) {
                    hideProgress()
                }

                override fun onCodeSent(verificationID: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@LoginActivity.verificationID = verificationID
                    resendToken = token
                    hideProgress()
                }
            }
        )
    }

    fun verifyOTP() {
        var otp = edt_otp.text.toString()
        if (BuildConfig.DEBUG){
            otp = "123456"
        }
    }

    fun showProgress() {
        pb_login.visible()
    }

    fun hideProgress() {
        pb_login.gone()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> {
                if (verificationID.isEmpty() && resendToken == null) {
                    var mobileNo = edt_mobile_no.text.toString()
                    if (BuildConfig.DEBUG) {
                        mobileNo = "+911234560986"
                    }
                    verifyPhoneNumber(mobileNo)
                } else {

                }
            }
        }
    }
}
