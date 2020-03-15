package com.pune.dance.fitness.ui.home

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.pune.dance.fitness.R
import com.pune.dance.fitness.api.attendance.AttendanceApiManager
import com.pune.dance.fitness.api.attendance.models.Attendance
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
    val nextSessionAttendanceLiveResult = LiveResult<AttendanceStatus>()

    private val user = userDao.getUser()
    private val userProfile = userDao.getUserProfile(getUserId())
    private val disposable by lazy { CompositeDisposable() }
    private var nextSessionAttendance: Attendance? = null

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
            .doOnSubscribe { attendanceLiveResult.loading() }
            .subscribeObserverOnMain()
            .subscribe({ calendarItems ->
                attendanceLiveResult.success(calendarItems)
            }, {
                logError(message = "Attendance fetch failure", throwable = it)
                attendanceLiveResult.error(error = it)
            })
            .addTo(disposable)
    }


    /**getting attendance for next session*/
    fun getNextSessionAttendance() {
        attendanceApiManager.getAttendance(getUserId(), getFitnessSessionId(), Date().stripTime(), getNextSessionDate())
            .map { attendanceList ->
                return@map if (attendanceList.isEmpty()) {
                    AttendanceStatus.UNKNOWN
                } else {
                    AttendanceStatus.from(attendanceList[0].status)
                }
            }
            .doOnSubscribe { nextSessionAttendanceLiveResult.loading() }
            .subscribeObserverOnMain()
            .subscribe({ status ->
                nextSessionAttendanceLiveResult.success(status)
            }, {
                logError(message = "Next session attendance fetch failure", throwable = it)
            })
            .addTo(disposable)
    }

    private fun getNextSessionDate(): Date {
        //todo: correct logic
        return Date()
    }

    /**set attendance status*/
    fun markAttendance(status: AttendanceStatus) {
        //creating attendance object to be saved
        if (nextSessionAttendance == null) {
            nextSessionAttendance = Attendance().apply {
                date = Timestamp(getNextSessionDate())
                sessionId = getFitnessSessionId()
                userId = getUserId()
            }
        }
        nextSessionAttendance?.status = status.value

        //api call
        attendanceApiManager.markAttendance(nextSessionAttendance!!)
            .doOnSubscribe { nextSessionAttendanceLiveResult.loading() }
            .subscribeObserverOnMain()
            .subscribe({
                nextSessionAttendanceLiveResult.success(status)
            }, {
                logError(message = "Next session attendance fetch failure", throwable = it)
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

    fun getAttendanceMsg(attendanceStatus: AttendanceStatus?): Int {
        return when (attendanceStatus) {
            AttendanceStatus.PRESENT -> R.string.attendance_marked_present_msg
            AttendanceStatus.ABSENT -> R.string.attendance_marked_absent_msg
            AttendanceStatus.UNKNOWN -> R.string.attendance_ask_presence_msg
            else -> R.string.error_unknown
        }
    }

    fun getAttendanceTitle(attendanceStatus: AttendanceStatus?): Int {
        return when (attendanceStatus) {
            AttendanceStatus.PRESENT -> R.string.attendance_marked_present_title
            AttendanceStatus.ABSENT -> R.string.attendance_marked_absent_title
            AttendanceStatus.UNKNOWN -> R.string.attendance_ask_presence
            else -> R.string.error_unknown
        }
    }

    fun getAttendancePrimaryAction(attendanceStatus: AttendanceStatus?): Int {
        return when (attendanceStatus) {
            AttendanceStatus.PRESENT -> R.string.empty_string
            AttendanceStatus.ABSENT -> R.string.attendance_marked_absent_action_primary
            AttendanceStatus.UNKNOWN -> R.string.attendance_ask_action_primary
            else -> R.string.empty_string
        }
    }

    fun getAttendanceSecondaryAction(attendanceStatus: AttendanceStatus?): Int {
        return when (attendanceStatus) {
            AttendanceStatus.PRESENT -> R.string.attendance_marked_present_action_secondary
            AttendanceStatus.ABSENT -> R.string.empty_string
            AttendanceStatus.UNKNOWN -> R.string.attendance_ask_action_secondary
            else -> R.string.empty_string
        }
    }

    fun getAttendanceDrawable(attendanceStatus: AttendanceStatus?): Int {
        return when (attendanceStatus) {
            AttendanceStatus.PRESENT -> R.drawable.art_attendance_present
            AttendanceStatus.ABSENT -> R.drawable.art_attendance_absent
            AttendanceStatus.UNKNOWN -> R.drawable.art_attendance_ask
            else -> R.drawable.art_attendance_ask
        }
    }

    fun getAttendanceColor(attendanceStatus: AttendanceStatus?): Int {
        return when (attendanceStatus) {
            AttendanceStatus.PRESENT -> R.color.attendance_present
            AttendanceStatus.ABSENT -> R.color.attendance_absent
            AttendanceStatus.UNKNOWN -> R.color.attendance_ask
            else -> R.color.attendance_ask
        }
    }

    fun getUserId() = user?.id ?: ""
    fun getFitnessSessionId() = userProfile?.fitness_session_id ?: ""

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}