package com.pune.dance.fitness.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.ui.home.models.MealItem
import kotlinx.android.synthetic.main.item_home_diet_item.view.*

class MealAdapter(private val interaction: Interaction? = null) :
    ListAdapter<MealItem, MealAdapter.MealViewHolder>(MealItemDiffCallback()) {

    private val MAX_FOOD_ITEMS = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MealViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_home_diet_item, parent, false), interaction
    )

    override fun getItemCount(): Int {
        return Math.min(super.getItemCount(), MAX_FOOD_ITEMS)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) = holder.bind(getItem(position))

    fun update(data: List<MealItem>) {
        submitList(data.toMutableList())
    }

    /**View holder*/
    inner class MealViewHolder(itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            val clicked = getItem(adapterPosition)
        }

        fun bind(mealItem: MealItem) = with(itemView) {
            tv_meal_item.text = mealItem.mealName
        }
    }

    /**Diff callback*/
    private class MealItemDiffCallback : DiffUtil.ItemCallback<MealItem>() {

        override fun areItemsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
            return oldItem == newItem
        }
    }


    interface Interaction
}