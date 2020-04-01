package com.pune.dance.fitness.ui.diet

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.diet.DietPlanApiManager
import com.pune.dance.fitness.api.diet.models.DietPlan
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.subscribeObserverOnMain
import com.pune.dance.fitness.data.UserDao
import io.reactivex.disposables.CompositeDisposable

class DietViewModel : ViewModel() {


    private val userDao = UserDao()
    private val dietPlanApiManager = DietPlanApiManager()

    val dietPlanLiveResult = LiveResult<DietPlan>()

    private val user = userDao.getUser()
    private val userProfile = userDao.getUserProfile(user?.id ?: "")

    private val disposable by lazy { CompositeDisposable() }

    fun fetchDietPlan() {
        dietPlanApiManager.getDietPlan(userProfile?.dietPlanId ?: "")
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