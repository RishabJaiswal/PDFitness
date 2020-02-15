package com.pune.dance.fitness.features.login

import android.os.Bundle
import android.view.View
import com.pune.dance.fitness.BuildConfig
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.application.extensions.gone
import com.pune.dance.fitness.application.extensions.visible
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    private val viewModel by lazy {
        configureViewModel { LoginViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener(this)
    }

    fun verifyPhoneNumber(mobileNo: String) {
        showProgress()
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

            }
        }
    }
}
