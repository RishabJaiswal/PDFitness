package com.pune.dance.fitness.ui.external

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.R

class AdRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_request)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AdRequestActivity::class.java)
        }
    }
}
