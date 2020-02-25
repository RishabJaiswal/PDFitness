package com.pune.dance.fitness

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.data.UserDao
import com.pune.dance.fitness.ui.login.LoginActivity
import com.pune.dance.fitness.ui.profile.edit.EditProfileActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserDao().getUser()?.let {
            startActivity(EditProfileActivity.getIntent(this))
        } ?: startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
