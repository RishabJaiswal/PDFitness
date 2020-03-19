package com.pune.dance.fitness.ui.home

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.pune.dance.fitness.R
import com.pune.dance.fitness.api.attendance.AttendanceApiManager
import com.pune.dance.fitness.api.attendance.models.Attendance
import com.pune.dance.fitness.api.diet.DietPlanApiManager
import com.pune.dance.fitness.api.profile.FitnessApiManager
import com.pune.dance.fitness.api.profile.models.FitnessSession
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.logError
import com.pune.dance.fitness.application.extensions.stripTime
import com.pune.dance.fitness.application.extensions.subscribeObserverOnMain
import com.pune.dance.fitness.data.UserDao
import com.pune.dance.fitness.ui.home.models.*
import io.reactivex.disposables.CompositeDisposable
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val userDao = UserDao()
    private val dietPlanApiManager = DietPlanApiManager()
    private val attendanceApiManager = AttendanceApiManager()
    private val fitnessApiManager = FitnessApiManager()

    val dietPlanLiveResult = LiveResult<List<DietPlanItem>>()
    val attendanceLiveResult = LiveResult<List<CalendarItem>>()
    val nextSessionAttendanceLiveResult = LiveResult<AttendanceStatus>()
    lateinit var nextSessionDate: Date
    private var nextSessionAttendance: Attendance? = null
    private var fitnessSession: FitnessSession? = null

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


    /**get user's fitness session*/
    fun fetchFitnessSession() {
        fitnessApiManager.getFitnessSessionById(getFitnessSessionId())
            .subscribeObserverOnMain()
            .doOnSubscribe { nextSessionAttendanceLiveResult.loading() }
            .subscribe({ session ->
                fitnessSession = session
                extractNextSessionDate(session)
                fetchNextSessionAttendance()
            }, {
                logError(message = "Unable to fetch fitness session", throwable = it)
                nextSessionAttendanceLiveResult.error(error = it)
            })
            .addTo(disposable)
    }

    /**getting attendance for next session*/
    fun fetchNextSessionAttendance() {
        attendanceApiManager.getAttendance(getUserId(), getFitnessSessionId(), Date().stripTime(), nextSessionDate)
            .map { attendanceList ->
                return@map if (attendanceList.isEmpty()) {
                    AttendanceStatus.UNKNOWN
                } else {
                    nextSessionAttendance = attendanceList[0]
                    AttendanceStatus.from(nextSessionAttendance?.status ?: "")
                }
            }
            .doOnSubscribe { nextSessionAttendanceLiveResult.loading() }
            .subscribeObserverOnMain()
            .subscribe({ status ->
                nextSessionAttendanceLiveResult.success(status)
            }, {
                logError(message = "Next session attendance fetch failure", throwable = it)
                nextSessionAttendanceLiveResult.error(error = it)
            })
            .addTo(disposable)
    }


    /**get the next date for a given fitness session*/
    private fun extractNextSessionDate(session: FitnessSession) {
        val now = Calendar.getInstance()

        //getting next session day
        var nextSessionDay = -1
        val currentDay = now.get(Calendar.DAY_OF_WEEK)
        session.days.forEach { day ->
            if (day >= currentDay) {
                nextSessionDay = day
                return@forEach
            }
        }

        //setting date and time
        val sdfDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val sdfTime = SimpleDateFormat("hh:mm:ss z", Locale.getDefault())
        val sdfNextSession = SimpleDateFormat("dd MMM yyyy hh:mm:ss z", Locale.getDefault())

        val sessionTime = fitnessSession?.timings?.get(0)?.time?.toDate() ?: Date()
        val nextSessionCalendar = Calendar.getInstance().apply {
            time = sdfNextSession.parse("${sdfDate.format(now.time)} ${sdfTime.format(sessionTime)}") ?: now.time
        }

        //getting next session day
        if (nextSessionDay == -1) {
            nextSessionDay = fitnessSession?.days?.get(0) ?: -1
            nextSessionCalendar.add(Calendar.WEEK_OF_MONTH, 1)
        }

        //setting final day, date, and clock time
        nextSessionCalendar.set(Calendar.DAY_OF_WEEK, nextSessionDay)

        nextSessionDate = nextSessionCalendar.time
    }

    /**set attendance status*/
    fun markAttendance(status: AttendanceStatus) {
        //creating attendance object to be saved
        if (nextSessionAttendance == null) {
            nextSessionAttendance = Attendance().apply {
                date = Timestamp(nextSessionDate)
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