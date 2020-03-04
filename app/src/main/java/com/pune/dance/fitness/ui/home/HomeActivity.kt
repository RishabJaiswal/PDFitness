package com.pune.dance.fitness.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.application.extensions.gone
import com.pune.dance.fitness.application.extensions.visible
import com.pune.dance.fitness.application.extensions.visibleOrGone
import com.pune.dance.fitness.ui.home.adapters.AttendanceCalendarAdapter
import com.pune.dance.fitness.ui.home.adapters.DietPlanAdapter
import com.pune.dance.fitness.ui.home.adapters.PaymentsAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val attendanceCalendarAdapter by lazy { AttendanceCalendarAdapter(this) }
    private val paymentsAdapter by lazy { PaymentsAdapter() }
    private val dietPlanAdapter by lazy { DietPlanAdapter() }

    private val viewModel by lazy {
        configureViewModel<HomeViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupAttendance()
        setupPayments()
        setupDietPlan()
    }

    private fun setupAttendance() {
        viewModel.getAttendanceCalendarItems()
        rv_attendance.adapter = attendanceCalendarAdapter
        viewModel.attendanceLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                //todo: add progress
            }, { attendanceCalendarItems ->
                //success
                attendanceCalendarAdapter.update(attendanceCalendarItems)
            }, {
                //error
                //todo: add error
            })
        })
    }

    private fun setupPayments() {
        rv_payments.adapter = paymentsAdapter
        paymentsAdapter.update(viewModel.getPaymentItems())
    }

    private fun setupDietPlan() {
        viewModel.getDietPlan()
        rv_diet_plan.adapter = dietPlanAdapter

        //observing user's diet plan
        viewModel.dietPlanLiveResult.observe(this, Observer {
            it.parseResult({
                //loading
                progress_diet_plan.visible()
            }, { dietPlanItems ->
                //success
                progress_diet_plan.gone()
                blankslate_diet_plan.visibleOrGone(dietPlanItems.isEmpty())
                dietPlanAdapter.update(dietPlanItems)

                //setting drawable for dietplan
                imv_diet_plan.setImageResource(
                    if (dietPlanItems.isEmpty())
                        R.drawable.blankslate_diet_plan
                    else
                        R.drawable.art_diet_plan
                )
            }, {
                //error
                progress_diet_plan.gone()
            })
        })
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
