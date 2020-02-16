package com.pune.dance.fitness.features.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.pune.dance.fitness.BuildConfig
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.application.extensions.gone
import com.pune.dance.fitness.application.extensions.invisible
import com.pune.dance.fitness.application.extensions.visible
import com.pune.dance.fitness.features.profile.edit.EditProfileActivity
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

        //observing verification of mobile no
        viewModel.verificationTokenLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                pb_get_otp.visible()
                btn_get_otp.gone()
            }, {
                //success
                pb_get_otp.gone()
                group_login_views.visible()
            }, {
                //error
                pb_get_otp.gone()
                btn_get_otp.visible()
            })
        })

        //observing verification of otp
        viewModel.loginLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                pb_login.visible()
                btn_login.invisible()
                btn_resend_otp.gone()
            }, {
                //success
                startActivity(EditProfileActivity.getIntent(this))
            }, {
                //error
                pb_login.gone()
                btn_login.visible()
                btn_resend_otp.visible()
            })
        })
    }


    private fun verifyPhoneNumber() {
        var mobileNo = edt_mobile_no.text.toString()
        if (BuildConfig.DEBUG) {
            mobileNo = "+911234560986"
        }
        viewModel.verifyMobileNumber(mobileNo)
    }

    private fun verifyOTP() {
        var otp = edt_otp.text.toString()
        if (BuildConfig.DEBUG){
            otp = "123456"
        }
        viewModel.verifyOTP(otp)
    }

    fun showProgress() {
        pb_login.visible()
    }

    fun hideProgress() {
        pb_login.gone()
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
