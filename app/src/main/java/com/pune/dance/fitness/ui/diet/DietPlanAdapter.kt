package com.pune.dance.fitness.ui.diet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.api.diet.models.Food
import kotlinx.android.synthetic.main.item_diet_plan.view.*

class DietPlanAdapter(private val interaction: Interaction? = null) :
    ListAdapter<Food, DietPlanAdapter.DietPlanViewHolder>(DietPlanItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DietPlanViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diet_plan, parent, false), interaction
    )

    override fun onBindViewHolder(holder: DietPlanViewHolder, position: Int) = holder.bind(getItem(position))

    fun update(data: List<Food>) {
        submitList(data.toMutableList())
    }

    /**View holder*/
    inner class DietPlanViewHolder(itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(food: Food) = with(itemView) {
            tv_food_name.text = food.name
        }
    }

    /**Diff callback*/
    private class DietPlanItemDiffCallback : DiffUtil.ItemCallback<Food>() {

        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }

    interface Interaction
}