package com.pune.dance.fitness.ui.login

import android.content.Context
import android.content.Intent
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
            }, {
                //success
                startActivity(EditProfileActivity.getIntent(this))
                finish()
            }, {
                //error
                pb_login.gone()
                if (viewModel.getOtpTimeLeft() == 0L) {
                    toggleLoginViews(isVisible = false)
                    toggleGetOtpViews(isVisible = true)
                } else {
                    btn_login.visible()
                }
                toast(R.string.error_invalid_otp)
            })
        })

        /**OTP timer*/
        viewModel.otpTimerMillisLeft.observe(this, Observer<Long> { millisLeft ->
            if (millisLeft == null || millisLeft <= 0) {
                onOtpTimerEnd()
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
     * related to verifying OTP and logging user in*
     * @param{isVisible} tells if user will be able to
     * enter the OTP*/
    private fun toggleLoginViews(isVisible: Boolean) {
        pb_get_otp.visibleOrGone(!isVisible)
        btn_login.visibleOrGone(isVisible)
        edt_otp.visibleOrGone(isVisible)
        group_timer.visibleOrGone(isVisible)
        edt_mobile_no.isEnabled = !isVisible

        //clearing previously enter OTPs
        if (isVisible) {
            edt_otp.setText("")
        }
        hideKeyboard(edt_otp)
    }

    /**hide/show views to showcase LCE for views
     * related to getting OTP*/
    private fun toggleGetOtpViews(isVisible: Boolean) {
        pb_get_otp.visibleOrGone(!isVisible)
        btn_get_otp.visibleOrGone(isVisible)
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
            return "${String.format("%02d", seconds.div(60))} : ${String.format("%02d", seconds.rem(60))}"
        }
        return null
    }

    private fun onOtpTimerEnd() {
        group_timer.gone()

        //check is user is currently logging in or not
        if (viewModel.loginLiveResult.isLoading().not()) {
            toggleLoginViews(isVisible = false)
            toggleGetOtpViews(isVisible = true)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            //get otp
            R.id.btn_get_otp -> {
                verifyPhoneNumber()
                hideKeyboard(view)
            }

            //login
            R.id.btn_login -> {
                verifyOTP()
                hideKeyboard(view)
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
