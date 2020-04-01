package com.pune.dance.fitness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.application.PreferenceHelper
import com.pune.dance.fitness.application.PreferenceKeys
import com.pune.dance.fitness.data.UserDao
import com.pune.dance.fitness.ui.login.LoginActivity
import com.pune.dance.fitness.ui.online.OnlineSessionActivity
import com.pune.dance.fitness.ui.profile.edit.EditProfileActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //choosing activity to launch
        var startIntent = LoginActivity.getIntent(this)
        UserDao().getUser()?.let {

            val hasUserCreatedProfile = PreferenceHelper.getValue<Boolean>(PreferenceKeys.USER_CREATED_PROFILE)
            startIntent = if (hasUserCreatedProfile) {
                OnlineSessionActivity.getIntent(this)
            } else {
                EditProfileActivity.getIntent(this)
            }
        }
        startActivity(startIntent)
        finish()
    }
}
