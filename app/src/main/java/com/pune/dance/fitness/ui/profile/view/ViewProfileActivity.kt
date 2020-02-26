package com.pune.dance.fitness.ui.profile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.ui.profile.edit.EditProfileActivity
import com.pune.dance.fitness.ui.profile.edit.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_view_profile.*

class ViewProfileActivity : AppCompatActivity(), View.OnClickListener {

    private val profileItemsAdapter by lazy { ViewProfileAdapter() }

    private val viewModel by lazy {
        configureViewModel<EditProfileViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        setupUserProfile()
        btn_edit_profile.setOnClickListener(this)
    }

    private fun setupUserProfile() {
        tv_profile_name.text = viewModel.getName()
        tv_phone_number.text = viewModel.getMobileNumber()
        rv_profile.adapter = profileItemsAdapter
        profileItemsAdapter.submitList(viewModel.getProfileItems())
    }

    override fun onClick(v: View?) {
        startActivity(EditProfileActivity.getIntent(this))
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ViewProfileActivity::class.java)
        }
    }
}
