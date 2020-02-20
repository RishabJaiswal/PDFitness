package com.pune.dance.fitness.features.profile.edit

import android.os.Bundle
import android.view.View
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseFragment
import com.pune.dance.fitness.application.extensions.configureViewModel
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

    override fun onClick(v: View?) {
        /**taking user home after updating profile*/
        viewModel.updateProfile()
    }
}