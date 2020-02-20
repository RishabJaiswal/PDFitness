package com.pune.dance.fitness.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.pune.dance.fitness.BuildConfig
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.*
import com.pune.dance.fitness.ui.profile.edit.EditProfileActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    private val viewModel by lazy {
        configureViewModel { LoginViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener(this)
        btn_get_otp.setOnClickListener(this)

        /**observing verification of mobile no*/
        viewModel.verificationTokenLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                toggleGetOtpViews(isVisible = false)
            }, {
                //success
                toggleLoginViews(isVisible = true)
            }, { error ->
                //error
                toggleGetOtpViews(isVisible = true)
                if (error is FirebaseAuthInvalidCredentialsException) {
                    toast(R.string.error_invalid_mobile_no)
                }
            })
        })

        /**observing verification of otp*/
        viewModel.loginLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                pb_login.visible()
                btn_login.invisible()
                btn_resend_otp.gone()
            }, {
                //success
                startActivity(EditProfileActivity.getIntent(this))
                finish()
            }, {
                //error
                pb_login.gone()
                btn_login.visible()
                toggleResendOtpButton(isVisible = true)
                toast(R.string.error_invalid_otp)
            })
        })

        /**OTP timer*/
        viewModel.otpTimerMillisLeft.observe(this, Observer<Long> { millisLeft ->
            if (millisLeft == null || millisLeft <= 0) {
                removeTimer()
            } else {
                tv_otp_timer.text = getRemainingTime(millisLeft)
            }
        })
    }

    override fun onDestroy() {
        viewModel.stopTimer()
        super.onDestroy()
    }

    /**hide/show views to showcase LCE for views
     * related to verifying OTP
     * and logging user in*/
    private fun toggleLoginViews(isVisible: Boolean) {
        pb_get_otp.visibleOrGone(!isVisible)
        btn_login.visibleOrGone(isVisible)
        toggleResendOtpButton(isVisible)
        edt_otp.visibleOrGone(isVisible)
    }

    /**hide/show views to showcase LCE for views
     * related to getting OTP*/
    private fun toggleGetOtpViews(isVisible: Boolean) {
        pb_get_otp.visibleOrGone(!isVisible)
        btn_get_otp.visibleOrGone(isVisible)
    }

    /**otp edit text needs to be aware of the timer*/
    private fun toggleResendOtpButton(isVisible: Boolean) {
        if (viewModel.getOtpTimeLeft() <= 0) {
            edt_otp.gone()
        } else {
            edt_otp.visibleOrGone(isVisible)
        }
    }


    private fun verifyPhoneNumber() {
        var mobileNo = edt_mobile_no.text.toString()
        if (mobileNo.isEmpty()) {
            toast(R.string.error_invalid_mobile_no)
        } else if (BuildConfig.DEBUG) {
            //only while debugging
            mobileNo = "+911234560986"
        }
        if (mobileNo.isNotEmpty()) {
            viewModel.verifyMobileNumber(mobileNo)
        }
    }

    private fun verifyOTP() {
        var otp = edt_otp.text.toString()
        if (otp.isEmpty()) {
            toast(R.string.error_invalid_otp)
        } else if (BuildConfig.DEBUG) {
            //only while debugging
            otp = "123456"
        }
        if (otp.isNotEmpty()) {
            viewModel.verifyOTP(otp)
        }
    }

    //getting time left for OTP expiration
    private fun getRemainingTime(millisLeft: Long): String? {
        if (millisLeft > 0) {
            val seconds = millisLeft / 1000
            return getString(
                R.string.otp_timer_msg,
                "${String.format("%02d", seconds.div(60))} : ${String.format("%02d", seconds.rem(60))}"
            )
        }
        return null
    }

    private fun removeTimer() {
        tv_otp_timer.gone()
        btn_resend_otp.visible()
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            //get otp
            R.id.btn_get_otp -> {
                verifyPhoneNumber()
            }

            //login
            R.id.btn_login -> {
                verifyOTP()
            }
        }
    }
}
