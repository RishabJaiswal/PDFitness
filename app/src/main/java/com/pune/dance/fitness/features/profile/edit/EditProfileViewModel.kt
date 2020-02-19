package com.pune.dance.fitness.features.profile.edit

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.profile.FitnessApiManager
import com.pune.dance.fitness.api.profile.models.FitnessSession
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.logError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class EditProfileViewModel : ViewModel() {

    val fitnessSessionsLiveResult = LiveResult<List<FitnessSession>>()
    private val disposable = CompositeDisposable()

    private val fitnessApiManager by lazy {
        FitnessApiManager()
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

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}