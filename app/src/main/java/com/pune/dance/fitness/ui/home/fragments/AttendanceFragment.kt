package com.pune.dance.fitness.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.databinding.FragHomeAttendanceBinding
import com.pune.dance.fitness.ui.home.HomeViewModel

class AttendanceFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragHomeAttendanceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_home_attendance, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = configureViewModel()
        binding.viewModel = viewModel
        binding.fragment = this
        viewModel.getNextSessionAttendance()
        observeNextSessionAttendance()
    }

    private fun observeNextSessionAttendance() {
        viewModel.nextSessionAttendanceLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                //todo: add progress
            }, {
                //success

            }, {
                //error
                //todo: add error
            })
        })
    }

    companion object {
        fun newInstance() = AttendanceFragment()
    }
}
