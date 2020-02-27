package com.pune.dance.fitness.ui.home

import androidx.lifecycle.ViewModel
import java.util.*

class HomeViewModel : ViewModel() {


    /**create data or attendance calendar*/
    fun getAttendanceCalendarItems(): List<CalendarItem> {
        val calendarItems = arrayListOf<CalendarItem>()

        //find min and max dates for current month
        val calendar = Calendar.getInstance()
        val minDay = 1
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //creating calendar item
        for (day in minDay..maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendarItems.add(CalendarItem(calendar.time))
        }
        return calendarItems
    }

}