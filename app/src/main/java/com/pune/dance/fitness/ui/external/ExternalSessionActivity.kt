package com.pune.dance.fitness.ui.external

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.BaseActivity
import com.pune.dance.fitness.application.extensions.configureViewModel
import com.pune.dance.fitness.application.extensions.dpToPixels
import com.pune.dance.fitness.application.extensions.gone
import com.pune.dance.fitness.application.extensions.visible
import com.pune.dance.fitness.databinding.ActivityExternalSessionBinding
import kotlinx.android.synthetic.main.activity_external_session.*

class ExternalSessionActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityExternalSessionBinding

    private val viewModel by lazy {
        configureViewModel<ExternalSessionViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_external_session)
        viewModel.fetchFitnessSessions()
        observeFitnessSession()
        binding.btnSessionLink.setOnClickListener(this)
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

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.btn_session_link -> {
                binding.onlineSession?.let { onlineSession ->
                    if (onlineSession.link.isEmpty()) {

                        //empty link
                        createToast(getString(R.string.hint_offtime_session, binding.onlineSession?.directions))
                            .apply {
                                setGravity(Gravity.TOP, 0, 16.dpToPixels(this@ExternalSessionActivity))
                                show()
                            }
                    }
                }
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ExternalSessionActivity::class.java)
        }
    }
}
