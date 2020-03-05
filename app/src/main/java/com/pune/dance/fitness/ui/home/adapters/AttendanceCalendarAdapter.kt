package com.pune.dance.fitness.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.getColor
import com.pune.dance.fitness.application.extensions.getDrawable
import com.pune.dance.fitness.ui.home.models.AttendanceStatus
import com.pune.dance.fitness.ui.home.models.CalendarItem
import kotlinx.android.synthetic.main.item_home_attendance_calendar.view.*
import java.text.SimpleDateFormat

class AttendanceCalendarAdapter(
    private val context: Context,
    private val interaction: Interaction? = null
) :
    ListAdapter<CalendarItem, AttendanceCalendarAdapter.CalendarViewHolder>(CalendarItemDiffCallback()) {

    private val sdf = SimpleDateFormat("dd")
    private val presentAttendeeColor by lazy { R.color.grapePurple.getColor(context) }
    private val absentAttendeeColor by lazy { R.color.tomato.getColor(context) }
    private val unknownAttendeeColor by lazy { R.color.textDisabled.getColor(context) }
    private val absentAttendeeBg by lazy { R.drawable.rectangle_tomato_stroked_rc_10.getDrawable(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CalendarViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_attendance_calendar, parent, false), interaction
    )

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun update(data: List<CalendarItem>) {
        submitList(data.toMutableList())
    }

    /**View holder*/
    inner class CalendarViewHolder(itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: CalendarItem) = with(itemView) {
            tv_date.apply {
                text = sdf.format(item.date)
                when (item.status) {

                    AttendanceStatus.PRESENT -> {
                        //user present
                        setTextColor(presentAttendeeColor)
                        background = null
                    }
                    AttendanceStatus.ABSENT -> {
                        //user absent
                        setTextColor(absentAttendeeColor)
                        background = absentAttendeeBg
                    }
                    else -> {
                        //user status unknown
                        setTextColor(unknownAttendeeColor)
                        background = null
                    }
                }
            }
        }

        //returns a small text for the calendar item
        private fun getShortDetailText(item: CalendarItem): String {
            val textRes = when (item.status) {

                AttendanceStatus.PRESENT -> {
                    //user present
                    R.string.attendance_status_present
                }
                AttendanceStatus.ABSENT -> {
                    //user absent
                    R.string.attendance_status_absent
                }
                else -> {
                    //user status unknown
                    R.string.attendance_status_no_session
                }
            }
            return context.getString(textRes)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            val clickedItem = getItem(adapterPosition)

            //setting changes in date text for the calendar item
            itemView.tv_date.apply {
                text = getShortDetailText(clickedItem)
                //setting text back to calendar date after few seconds
                handler.postDelayed({
                    text = sdf.format(clickedItem.date)
                }, 2000L)
            }
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