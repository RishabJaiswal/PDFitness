package com.pune.dance.fitness.ui.home

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.ui.home.models.CalendarItem
import com.pune.dance.fitness.ui.home.models.DietPlanItem
import com.pune.dance.fitness.ui.home.models.MealItem
import com.pune.dance.fitness.ui.home.models.PaymentItem
import java.util.*

class HomeViewModel : ViewModel() {


    /**create data or attendance calendar*/
    fun getAttendanceCalendarItems(): List<CalendarItem> {
        val calendarItems = arrayListOf<CalendarItem>()

        //resetting to current month
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        //find min and max dates for current month
        val minDay = 1
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //creating calendar item
        for (day in minDay..maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendarItems.add(CalendarItem(calendar.time))
        }
        return calendarItems
    }


    /**create data or payments*/
    fun getPaymentItems(): List<PaymentItem> {
        val paymentItems = arrayListOf<PaymentItem>()

        //resetting to current month
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        //find min and max dates for current year
        val minMonth = 0
        val maxMonth = calendar.getActualMaximum(Calendar.MONTH)

        //creating payment item
        for (month in minMonth..maxMonth) {
            calendar.set(Calendar.MONTH, month)
            paymentItems.add(PaymentItem(calendar.time))
        }

        return paymentItems
    }

    fun getDietPlan(): List<DietPlanItem> {
        val dietPlanItems = arrayListOf<DietPlanItem>()
        dietPlanItems.add(
            DietPlanItem(
                arrayListOf(
                    MealItem("Poha"),
                    MealItem("Peanuts"),
                    MealItem("Boiled Broccoli")
                )
            )
        )
        return dietPlanItems
    }

}