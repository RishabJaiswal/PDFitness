package com.pune.dance.fitness.ui.profile.edit.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseFragment
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.application.extensions.invisible
import com.pune.dance.fitness.application.extensions.visible
import com.pune.dance.fitness.ui.external.ExternalSessionActivity
import com.pune.dance.fitness.ui.profile.edit.EditProfileViewModel
import kotlinx.android.synthetic.main.frag_edit_profile_done.*

class EditProfileDoneFragment : BaseFragment(), View.OnClickListener {

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { _activity ->
            viewModel = _activity.configureViewModel()
        }
    }

    override fun getLayout(): Int {
        return R.layout.frag_edit_profile_done
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_take_home.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        anim_confetti.playAnimation()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**observing updation of profile*/
        viewModel.profileUpdateLiveResult.observe(this, Observer {
            it.parseResult({
                btn_take_home.invisible()
                group_progress_update_profile.visible()
            }, {
                activity?.let { _activity ->
                    startActivity(ExternalSessionActivity.getIntent(_activity))
                    _activity.finish()
                }
            }, {
                toast(R.string.error_unknown)
            })
        })
    }

    override fun onClick(v: View?) {
        viewModel.updateProfile()
    }
}