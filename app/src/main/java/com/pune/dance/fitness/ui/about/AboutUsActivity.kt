package com.pune.dance.fitness.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.openLink
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        btn_visit_fb.setOnClickListener(this)
        btn_visit_insta.setOnClickListener(this)
        btn_visit_youtube.setOnClickListener(this)
        imv_logo.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.btn_visit_fb -> {
                getString(R.string.link_brand_fb).openLink(this)
            }

            R.id.btn_visit_insta -> {
                getString(R.string.link_brand_insta).openLink(this)
            }

            R.id.btn_visit_youtube -> {
                getString(R.string.link_brand_youtube).openLink(this)
            }

            R.id.imv_logo -> {
                getString(R.string.link_brand_website).openLink(this)
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AboutUsActivity::class.java)
        }
    }
}
