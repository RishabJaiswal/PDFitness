package com.pune.dance.fitness.ui.profile.edit.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseFragment
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.ui.profile.edit.EditProfileViewModel
import com.pune.dance.fitness.ui.profile.edit.adapters.SessionsAdapter
import kotlinx.android.synthetic.main.frag_edit_time_place.*

class EditTImePlaceFragment : BaseFragment() {

    private lateinit var viewModel: EditProfileViewModel
    private val sessionsAdapter by lazy { SessionsAdapter(this::onSessionTimingSelected) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { _activity ->
            viewModel = _activity.configureViewModel()
            viewModel.getFitnessSessions()
            observeLiveContent()
        }
    }

    override fun getLayout(): Int {
        return R.layout.frag_edit_time_place
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_time_place_slides.adapter = sessionsAdapter
    }

    private fun observeLiveContent() {

        /**observing list / LCE for fitness sessions' place and timings*/
        viewModel.fitnessSessionsLiveResult.observe(this, Observer {
            it.parseResult({
                //todo:: add progress
            }, { sessionsList ->
                sessionsAdapter.submitList(sessionsList)
            }, {
                //todo:: add error
            })
        })
    }

    /**updates user profile's fields for session and timing ID*/
    private fun onSessionTimingSelected(sessionId: String, timingId: String) {
        viewModel.updateUserFitnessSession(sessionId, timingId)
    }
}