package com.pune.dance.fitness.application

import android.app.Application
import io.realm.Realm

class PdfApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}