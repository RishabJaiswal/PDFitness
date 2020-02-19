package com.pune.dance.fitness.features.profile.edit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseFragment
import com.pune.dance.fitness.application.extensions.configureViewModel
import kotlinx.android.synthetic.main.frag_edit_time_place.*

class EditTImePlaceFragment : BaseFragment() {

    private lateinit var viewModel: EditProfileViewModel
    private val sessionsAdapter by lazy { SessionsAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { _activity ->
            viewModel = _activity.configureViewModel()
            viewModel.getFitnessSessions()
            observeSessionsList()
        }
    }

    override fun getLayout(): Int {
        return R.layout.frag_edit_time_place
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_time_place_slides.adapter = sessionsAdapter

    }

    /**displays list / LCE for fitness sessions' place and timings*/
    private fun observeSessionsList() {
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
}