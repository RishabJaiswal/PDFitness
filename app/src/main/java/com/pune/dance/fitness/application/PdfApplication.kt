package com.pune.dance.fitness.application

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.pune.dance.fitness.R
import io.realm.Realm

class PdfApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        initAdMob()
    }

    private fun initAdMob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }
}