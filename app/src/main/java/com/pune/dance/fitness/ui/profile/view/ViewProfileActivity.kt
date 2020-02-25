package com.pune.dance.fitness.ui.profile.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.ui.profile.edit.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_view_profile.*

class ViewProfileActivity : AppCompatActivity() {

    private val viewModel by lazy {
        configureViewModel<EditProfileViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        setupUserProfile()
    }

    private fun setupUserProfile() {
        tv_profile_name.text = viewModel.getName()
        tv_phone_number.text = viewModel.getMobileNumber()
    }
}
