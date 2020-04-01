package com.pune.dance.fitness.ui.diet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.application.extensions.gone
import com.pune.dance.fitness.application.extensions.visible
import kotlinx.android.synthetic.main.activity_diet_plan.*
import kotlinx.android.synthetic.main.activity_home.*

class DietPlanActivity : AppCompatActivity() {

    private val viewModel by lazy {
        configureViewModel<DietViewModel>()
    }
    private val dietPlanAdapter = DietPlanAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_plan)
        rv_diet_plan.adapter = dietPlanAdapter
        viewModel.fetchDietPlan()
        observeDietPlan()
    }

    private fun observeDietPlan() {
        viewModel.dietPlanLiveResult.observe(this, Observer {
            it.parseResult({
                pb_diet_plan.visible()
            }, { dietPlan ->
                pb_diet_plan.gone()
                //todo change logic
                dietPlanAdapter.update(dietPlan.meals[0].foodItems)
            }, {
                pb_diet_plan.gone()
            })
        })
    }
}
