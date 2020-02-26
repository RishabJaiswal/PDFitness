package com.pune.dance.fitness.ui.profile.edit.fragments

import android.os.Bundle
import android.view.View
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseFragment
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.ui.profile.edit.EditProfileViewModel
import com.pune.dance.fitness.ui.profile.view.ViewProfileActivity
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

    override fun onClick(v: View?) {
        viewModel.updateProfile()
        context?.let {
            startActivity(ViewProfileActivity.getIntent(it))
        }
    }
}