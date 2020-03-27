package com.pune.dance.fitness.ui.external

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pune.dance.fitness.BuildConfig
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.openLink
import com.pune.dance.fitness.ui.profile.edit.EditProfileActivity
import kotlinx.android.synthetic.main.frag_options_online_session_home.*

class OptionsBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_options_online_session_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_option_edit_profile.setOnClickListener(this)
        tv_option_about_us.setOnClickListener(this)
        tv_option_rate_app.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        context?.let { _context ->
            when (view?.id) {

                //edit profile
                R.id.tv_option_edit_profile -> {
                    startActivity(EditProfileActivity.getIntent(_context))
                }

                //about us
                R.id.tv_option_about_us -> {

                }

                //rate app
                R.id.tv_option_rate_app -> {
                    getString(R.string.link_app_play_store, BuildConfig.APPLICATION_ID)
                        .openLink(_context)
                }
            }
        }

    }

    companion object {
        fun newInstance(): OptionsBottomSheetFragment {
            return OptionsBottomSheetFragment()
        }
    }
}