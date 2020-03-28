package com.pune.dance.fitness.ui.online

import androidx.lifecycle.ViewModel
import com.pune.dance.fitness.api.profile.FitnessApiManager
import com.pune.dance.fitness.api.profile.models.FitnessSession
import com.pune.dance.fitness.application.LiveResult
import com.pune.dance.fitness.application.extensions.addTo
import com.pune.dance.fitness.application.extensions.logError
import com.pune.dance.fitness.application.extensions.subscribeObserverOnMain
import io.reactivex.disposables.CompositeDisposable

class ExternalSessionViewModel : ViewModel() {

    private val fitnessApiManager = FitnessApiManager()
    val sessionLiveResult = LiveResult<FitnessSession>()

    private val disposable = CompositeDisposable()

    fun fetchFitnessSessions() {
        fitnessApiManager.getFitnessSessionById("auto")
            .doOnSubscribe { sessionLiveResult.loading() }
            .subscribeObserverOnMain()
            .subscribe({ fitnessSession ->
                sessionLiveResult.success(fitnessSession)
            }, { error ->
                logError(message = "Unable to get auto fitness session", throwable = error)
                sessionLiveResult.error(error)
            })
            .addTo(disposable)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}