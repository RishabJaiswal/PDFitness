package com.pune.dance.fitness.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.ui.home.models.DietPlanItem
import kotlinx.android.synthetic.main.item_home_diet_plan.view.*

class DietPlanAdapter(private val interaction: Interaction? = null) :
    ListAdapter<DietPlanItem, DietPlanAdapter.DietPlanViewHolder>(DietPlanItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DietPlanViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_diet_plan, parent, false), interaction
    )

    override fun onBindViewHolder(holder: DietPlanViewHolder, position: Int) = holder.bind(getItem(position))

    fun update(data: List<DietPlanItem>) {
        submitList(data.toMutableList())
    }

    /**View holder*/
    inner class DietPlanViewHolder(itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView), OnClickListener {

        private val mealAdapter by lazy { MealAdapter() }

        init {
            itemView.setOnClickListener(this)
            itemView.rv_diet_meal_items.adapter = mealAdapter
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            val clicked = getItem(adapterPosition)
        }

        fun bind(dietPlanItem: DietPlanItem) = with(itemView) {
            tv_meal_name.text = dietPlanItem.mealName
            mealAdapter.update(dietPlanItem.foodItems)
        }
    }

    /**Diff callback*/
    private class DietPlanItemDiffCallback : DiffUtil.ItemCallback<DietPlanItem>() {

        override fun areItemsTheSame(oldItem: DietPlanItem, newItem: DietPlanItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DietPlanItem, newItem: DietPlanItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Interaction
}