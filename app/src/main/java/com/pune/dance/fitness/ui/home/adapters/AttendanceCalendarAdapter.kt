package com.pune.dance.fitness.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.ui.home.models.CalendarItem
import kotlinx.android.synthetic.main.item_home_attendance_calendar.view.*
import java.text.SimpleDateFormat

class AttendanceCalendarAdapter(private val interaction: Interaction? = null) :
    ListAdapter<CalendarItem, AttendanceCalendarAdapter.CalendarViewHolder>(CalendarItemDiffCallback()) {

    private val sdf = SimpleDateFormat("dd")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CalendarViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_attendance_calendar, parent, false), interaction
    )

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) = holder.bind(getItem(position))

    fun update(data: List<CalendarItem>) {
        submitList(data.toMutableList())
    }

    /**View holder*/
    inner class CalendarViewHolder(itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            val clicked = getItem(adapterPosition)
        }

        fun bind(item: CalendarItem) = with(itemView) {
            tv_date.text = sdf.format(item.date)
        }
    }

    /**Diff callback*/
    private class CalendarItemDiffCallback : DiffUtil.ItemCallback<CalendarItem>() {

        override fun areItemsTheSame(oldItem: CalendarItem, newItem: CalendarItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CalendarItem, newItem: CalendarItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Interaction
}