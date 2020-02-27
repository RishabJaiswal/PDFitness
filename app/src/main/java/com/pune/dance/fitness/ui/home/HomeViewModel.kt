package com.pune.dance.fitness.ui.home

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.diet.DietPlanApiManager
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.subscribeObserverOnMain
import com.pune.dance.fitness.data.UserDao
import com.pune.dance.fitness.ui.home.models.CalendarItem
import com.pune.dance.fitness.ui.home.models.DietPlanItem
import com.pune.dance.fitness.ui.home.models.MealItem
import com.pune.dance.fitness.ui.home.models.PaymentItem
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class HomeViewModel : ViewModel() {

    private val userDao = UserDao()
    private val dietPlanApiManager = DietPlanApiManager()
    val dietPlanLiveResult = LiveResult<List<DietPlanItem>>()

    private val user = userDao.getUser()
    private val userProfile = userDao.getUserProfile(user?.id ?: "")
    private val disposable by lazy { CompositeDisposable() }

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

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}