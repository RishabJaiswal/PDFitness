package com.pune.dance.fitness.features.profile.edit

import android.os.Bundle
import android.view.View
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseFragment
import com.pune.dance.fitness.application.extensions.configureViewModel
import kotlinx.android.synthetic.main.frag_edit_profile_name.*

class EditNameFragment : BaseFragment(), View.OnClickListener {


    private lateinit var viewModel: EditProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { _activity ->
            viewModel = _activity.configureViewModel()
        }
    }

    override fun getLayout(): Int {
        return R.layout.frag_edit_profile_name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next_edit_bio.setOnClickListener(this)

        //setting saved display name and cursor selection
        edt_name.apply {
            val displayName = viewModel.getName()
            setText(viewModel.getName())
            setSelection(displayName.length)
        }
    }

    override fun onClick(view: View?) {

        //setting user's display name
        val displayName = edt_name.text.toString().trim()
        if (displayName.isEmpty()) {
            toast(R.string.error_invalid_name)
        } else {
            viewModel.updateName(displayName)
            viewModel.changeEditProfileSliderPage()
        }
    }
}