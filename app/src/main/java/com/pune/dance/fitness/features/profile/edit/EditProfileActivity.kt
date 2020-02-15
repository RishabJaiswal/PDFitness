package com.pune.dance.fitness.features.profile.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity

class EditProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, EditProfileActivity::class.java)
        }
    }
}
