package com.pune.dance.fitness.ui.profile.edit

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.ui.profile.edit.adapters.EditProfileAdapter
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : BaseActivity() {

    private val viewModel by lazy { configureViewModel<EditProfileViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setEditProfileSlides()

        /**observing change in slider page*/
        viewModel.changePageLiveData.observe(this, Observer { pageIndex ->
            vp_edit_profile.setCurrentItem(pageIndex, true)
        })
    }

    private fun setEditProfileSlides() {
        val editProfileAdapter = EditProfileAdapter(supportFragmentManager, lifecycle)
        vp_edit_profile.adapter = editProfileAdapter
        vp_edit_profile.isUserInputEnabled = false
        pb_edit_profile.max = editProfileAdapter.itemCount

        vp_edit_profile.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pb_edit_profile.setProgress(position + 1, true)
                } else {
                    pb_edit_profile.progress = position + 1
                }
            }
        })
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, EditProfileActivity::class.java)
        }
    }
}
