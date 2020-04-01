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
import com.pune.dance.fitness.application.extensions.getColor
import com.pune.dance.fitness.application.extensions.getDrawable
import com.pune.dance.fitness.databinding.FragHomeAttendanceBinding
import com.pune.dance.fitness.ui.home.HomeViewModel
import com.pune.dance.fitness.ui.home.models.AttendanceStatus
import kotlinx.android.synthetic.main.frag_home_attendance.*

class AttendanceFragment : Fragment(), View.OnClickListener {

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
        binding.btnActionPrimary.setOnClickListener(this)
        binding.btnActionSecondary.setOnClickListener(this)
        viewModel.fetchFitnessSession()
        observeNextSessionAttendance()
    }

    private fun observeNextSessionAttendance() {
        viewModel.nextSessionAttendanceLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                //todo: add progress
            }, { attendanceStatus ->
                //success
                context?.let { _context ->

                    //image
                    imv_attendance.setImageDrawable(
                        viewModel.getAttendanceDrawable(attendanceStatus)
                            .getDrawable(_context)
                    )

                    //attendance scrim color
                    scrim_attendance.setBackgroundColor(
                        viewModel.getAttendanceColor(attendanceStatus)
                            .getColor(_context)
                    )
                }
            }, {
                //error
                //todo: add error
            })
        })
    }


    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.btn_action_primary -> {
                viewModel.markAttendance(AttendanceStatus.PRESENT)
            }

            R.id.btn_action_secondary -> {
                viewModel.markAttendance(AttendanceStatus.ABSENT)
            }
        }
    }


    companion object {
        fun newInstance() = AttendanceFragment()
    }
}
