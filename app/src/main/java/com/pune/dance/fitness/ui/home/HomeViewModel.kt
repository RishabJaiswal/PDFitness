package com.pune.dance.fitness.ui.home

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.attendance.AttendanceApiManager
import com.pune.dance.fitness.api.diet.DietPlanApiManager
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.logError
import com.pune.dance.fitness.application.extensions.stripTime
import com.pune.dance.fitness.application.extensions.subscribeObserverOnMain
import com.pune.dance.fitness.data.UserDao
import com.pune.dance.fitness.ui.home.models.*
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class HomeViewModel : ViewModel() {

    private val userDao = UserDao()
    private val dietPlanApiManager = DietPlanApiManager()
    private val attendanceApiManager = AttendanceApiManager()
    val dietPlanLiveResult = LiveResult<List<DietPlanItem>>()
    val attendanceLiveResult = LiveResult<List<CalendarItem>>()

    private val user = userDao.getUser()
    private val userProfile = userDao.getUserProfile(getUserId())
    private val disposable by lazy { CompositeDisposable() }

    /**create data or attendance calendar*/

    fun getAttendanceCalendarItems() {

        //resetting to current month
        val calendar = Calendar.getInstance()

        //find min and max dates for current month
        val minDay = 1
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //start date
        calendar.set(Calendar.DAY_OF_MONTH, minDay)
        val startDate = calendar.time
        //end date
        calendar.set(Calendar.DAY_OF_MONTH, maxDay)
        val endDate = calendar.time

        attendanceApiManager.getAttendance(getUserId(), getFitnessSessionId(), startDate, endDate)
            .map { attendanceList ->
                val calendarItems = arrayListOf<CalendarItem>()

                //creating calendar item
                for (day in minDay..maxDay) {
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    val calendarItem = CalendarItem(calendar.time)

                    //checking/setting for attendance status
                    attendanceList.filter { attendance ->
                        attendance.date.toDate().stripTime() == calendarItem.date.stripTime()
                    }.map { attendance ->
                        calendarItem.status = AttendanceStatus.from(attendance.status)
                    }
                    calendarItems.add(calendarItem)
                }
                return@map calendarItems
            }
            .subscribeObserverOnMain()
            .subscribe({ calendarItems ->
                attendanceLiveResult.success(calendarItems)
            }, {
                logError(message = "Attendance failure", throwable = it)
                attendanceLiveResult.error(error = it)
            })
            .addTo(disposable)
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

    fun getDietPlan() {
        dietPlanApiManager.getDietPlan(userProfile?.dietPlanId ?: "")
            .map { dietPlan ->
                val dietPlanItems = arrayListOf<DietPlanItem>()

                //creating diet plant items
                for (meal in dietPlan.meals) {

                    //creating meals items for the list
                    val mealItems = arrayListOf<MealItem>().apply {
                        for (food in meal.foodItems) {
                            add(MealItem(food.name))
                        }
                    }

                    //adding a diet plan meals
                    dietPlanItems.add(DietPlanItem(meal.mealName, mealItems))
                }
                return@map dietPlanItems
            }
            .doOnSubscribe { dietPlanLiveResult.loading() }
            .subscribeObserverOnMain()
            .subscribe({ dietPlanItems ->
                dietPlanLiveResult.success(dietPlanItems)
            }, {
                dietPlanLiveResult.error(error = it)
            })
            .addTo(disposable)
    }

    fun getUserId() = user?.id ?: ""
    fun getFitnessSessionId() = userProfile?.fitness_session_id ?: ""

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}