package com.pune.dance.fitness.ui.profile.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
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
        vp_edit_profile.adapter =
            EditProfileAdapter(supportFragmentManager, lifecycle)

        /**observing updation of profile*/
        viewModel.profileUpdateLiveResult.observe(this, Observer {
            it.parseResult({
                //todo:: add progress
            }, {
                //todo: start home screen
            }, {
                toast(R.string.error_unknown)
            })
        })

        /**observing change in slider page*/
        viewModel.changePageLiveData.observe(this, Observer { pageIndex ->
            vp_edit_profile.setCurrentItem(pageIndex, true)
        })
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, EditProfileActivity::class.java)
        }
    }
}
