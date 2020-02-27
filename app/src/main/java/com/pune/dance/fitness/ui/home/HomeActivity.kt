package com.pune.dance.fitness.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.ui.home.adapters.AttendanceCalendarAdapter
import com.pune.dance.fitness.ui.home.adapters.PaymentsAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val attendanceCalendarAdapter by lazy { AttendanceCalendarAdapter() }
    private val paymentsAdapter by lazy { PaymentsAdapter() }

    private val viewModel by lazy {
        configureViewModel<HomeViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupAttendance()
        setupPayments()
    }

    private fun setupAttendance() {
        rv_attendance.adapter = attendanceCalendarAdapter
        attendanceCalendarAdapter.update(viewModel.getAttendanceCalendarItems())
    }

    private fun setupPayments() {
        rv_payments.adapter = paymentsAdapter
        paymentsAdapter.update(viewModel.getPaymentItems())
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
