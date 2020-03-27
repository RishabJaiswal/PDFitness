package com.pune.dance.fitness.ui.external

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.*
import com.pune.dance.fitness.databinding.ActivityExternalSessionBinding
import kotlinx.android.synthetic.main.activity_external_session.*

class ExternalSessionActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityExternalSessionBinding
    private val AD_REQUEST_CODE = 108

    private val viewModel by lazy {
        configureViewModel<ExternalSessionViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_external_session)
        viewModel.fetchFitnessSessions()
        observeFitnessSession()
        binding.btnSessionLink.setOnClickListener(this)
        binding.btnShareSessionLink.setOnClickListener(this)
        binding.hasWatchedAd = false
    }

    private fun observeFitnessSession() {
        viewModel.sessionLiveResult.observe(this, Observer {
            it.parseResult({
                pb_session.visible()
            }, { session ->
                pb_session.gone()
                group_session_views.visible()
                binding.onlineSession = session.onlineSession
            }, {
                pb_session.gone()
                toast(R.string.error_unknown)
            })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AD_REQUEST_CODE) {
            when (resultCode) {

                //user watched the ad
                Activity.RESULT_OK -> {
                    binding.hasWatchedAd = true
                    binding.onlineSession?.link?.openLink(this)
                }

                //user didn't watch the ad
                Activity.RESULT_CANCELED -> {
                    toast(R.string.error_online_session)
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.btn_session_link -> {
                binding.onlineSession?.let { onlineSession ->
                    when {

                        //empty link
                        onlineSession.link.isEmpty() -> {
                            createToast(getString(R.string.hint_offtime_session, binding.onlineSession?.directions))
                                .apply {
                                    setGravity(Gravity.TOP, 0, 16.dpToPixels(this@ExternalSessionActivity))
                                    show()
                                }
                        }

                        //rewarded ad not watched
                        binding.hasWatchedAd != true -> startActivityForResult(
                            AdRequestActivity.getIntent(this),
                            AD_REQUEST_CODE
                        )

                        else -> {
                            onlineSession.link.openLink(this)
                        }
                    }
                }
            }

            //sharing session
            R.id.btn_share_session_link -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        getString(R.string.join_session_hint, binding.onlineSession?.linkForSharing)
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ExternalSessionActivity::class.java)
        }
    }
}
