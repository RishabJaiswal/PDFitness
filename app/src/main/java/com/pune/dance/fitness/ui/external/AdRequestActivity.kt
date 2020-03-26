package com.pune.dance.fitness.ui.external

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.gone
import com.pune.dance.fitness.application.extensions.logError
import com.pune.dance.fitness.application.extensions.visible
import kotlinx.android.synthetic.main.activity_ad_request.*

class AdRequestActivity : BaseActivity(), View.OnClickListener {

    private lateinit var rewardedAd: RewardedAd
    private lateinit var adLoadCallback: RewardedAdLoadCallback
    private lateinit var adShowCallback: RewardedAdCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_request)
        rewardedAd = RewardedAd(this, getString(R.string.watch_online_session_ad_unit_id))
        initAdCallbacks()
        btn_watch_ad.setOnClickListener(this)
    }

    private fun initAdCallbacks() {

        //ad loading callbacks
        adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
                if (rewardedAd.isLoaded) {
                    rewardedAd.show(this@AdRequestActivity, adShowCallback)
                }
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                logError(
                    tag = "AdRequest Error: $errorCode",
                    message = "Failed to load rewarded ad"
                )
                pb_ad_load.gone()
                btn_watch_ad.visible()
                toast(R.string.error_ad_load)
                finish()
            }
        }

        //ad showing callbacks
        adShowCallback = object : RewardedAdCallback() {
            override fun onRewardedAdOpened() {
                // Ad opened.
                pb_ad_load.gone()
                btn_watch_ad.visible()
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                // User earned reward.
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_watch_ad -> {
                //load ad
                rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
                pb_ad_load.visible()
                btn_watch_ad.gone()
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AdRequestActivity::class.java)
        }
    }
}
