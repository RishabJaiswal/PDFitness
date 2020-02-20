package com.pune.dance.fitness.ui.profile.edit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.api.profile.models.FitnessSessionTiming
import com.pune.dance.fitness.application.extensions.toString
import kotlinx.android.synthetic.main.item_select_time.view.*

class FitnessSessionTimingsAdapter(
    val fitnessSessionId: String,
    val onTimeSelected: (sessionId: String, timingId: String) -> Unit
) :
    ListAdapter<FitnessSessionTiming, FitnessSessionTimingsAdapter.TimingViewHolder>(
        TimingDiffcallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimingViewHolder {
        return TimingViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_select_time, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TimingViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var timing: FitnessSessionTiming

        init {
            itemView.btn_time.setOnClickListener(this)
        }

        fun bind(timing: FitnessSessionTiming) {
            this.timing = timing
            itemView.apply {
                btn_time.text = timing.time.toString("hh:mm aa")
                tv_price.text = timing.price.toString()
            }
        }

        override fun onClick(view: View?) {
            onTimeSelected(fitnessSessionId, timing.id)
        }
    }

    //diff callback for this listing
    class TimingDiffcallback : DiffUtil.ItemCallback<FitnessSessionTiming>() {

        override fun areItemsTheSame(oldItem: FitnessSessionTiming, newItem: FitnessSessionTiming): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FitnessSessionTiming, newItem: FitnessSessionTiming): Boolean {
            return oldItem == newItem
        }
    }
}