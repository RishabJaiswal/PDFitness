package com.pune.dance.fitness.ui.profile.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.profile.FitnessApiManager
import com.pune.dance.fitness.api.profile.models.FitnessSession
import com.pune.dance.fitness.api.user.UserApiManager
import com.pune.dance.fitness.api.user.models.UserProfile
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.*
import com.pune.dance.fitness.data.UserDao
import com.pune.dance.fitness.ui.profile.view.ProfileItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class EditProfileViewModel : ViewModel() {

    private val userDao = UserDao()
    private val fitnessApiManager by lazy { FitnessApiManager() }
    private val userApiManager by lazy { UserApiManager() }
    private val disposable = CompositeDisposable()

    private val user = userDao.getUser()?.asNonManagedRealmCopy()
    private val userProfile = userDao.getUserProfile(user?.id ?: "")?.asNonManagedRealmCopy()

    val fitnessSessionsLiveResult = LiveResult<List<FitnessSession>>()
    val profileUpdateLiveResult = LiveResult<UserProfile>()
    val changePageLiveData = MutableLiveData<Int>().apply {
        value = 0
    }

    /**get list of fitness with places and timings*/
    fun getFitnessSessions() {
        fitnessApiManager.getFitnessSessions()
            .doOnSubscribe { fitnessSessionsLiveResult.loading() }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ sessions ->
                fitnessSessionsLiveResult.success(sessions)
            }, {
                logError(throwable = it)
                fitnessSessionsLiveResult.error(error = it)
            })
            .addTo(disposable)
    }


    //setting and getting name
    fun getName() = userProfile?.displayName ?: ""

    fun getMobileNumber() = userProfile?.mobileNo ?: ""

    fun updateName(name: String) {
        userProfile?.displayName = name
    }

    fun updateUserFitnessSession(sessionId: String, timingId: String) {
        userProfile?.fitness_session_id = sessionId
        userProfile?.session_timing_id = timingId
    }

    fun updateProfile() {
        userProfile?.let {
            userApiManager.setUserProfile(user?.id ?: "", userProfile)
                .doOnSubscribe { profileUpdateLiveResult.loading() }
                .subscribeObserverOnMain()
                .flatMapResumeAfter { userDao.saveAsync(it) }
                .subscribe({ profile ->
                    profileUpdateLiveResult.success(profile)
                }, {
                    logError(throwable = it)
                    profileUpdateLiveResult.error(it)
                })
        } ?: profileUpdateLiveResult.error(NullPointerException("user profile not found"))
    }

    /**changes the slider page to the next one*/
    fun changeEditProfileSliderPage() {
        changePageLiveData.value?.let { pageIndex ->
            changePageLiveData.value = pageIndex + 1
        }
    }

    fun getProfileItems(): List<ProfileItem> {
        return arrayListOf(
            ProfileItem("Participation ratio", "95%"),
            ProfileItem("Diet plan", "3 week"),
            ProfileItem("Fees due", "400 /-"),
            ProfileItem("Timing", "7: 30pm"),
            ProfileItem.VenueItem("1.22", "2.22").apply {
                key = "Venue"
                value = "Place name"
            })
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}