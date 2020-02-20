package com.pune.dance.fitness.features.profile.edit

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import com.pune.dance.fitness.api.profile.models.FitnessSession
import kotlinx.android.synthetic.main.item_select_time_place.view.*

class SessionsAdapter : ListAdapter<FitnessSession, SessionsAdapter.SessionViewHolder>(SessionDiffcallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_select_time_place, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var fitnessSession: FitnessSession
        private val timingsAdapter = FitnessSessionTimingsAdapter()

        fun bind(fitnessSession: FitnessSession) {
            this.fitnessSession = fitnessSession
            itemView.apply {
                tv_place.text = fitnessSession.displayAddress
                btn_directions.setOnClickListener(this@SessionViewHolder)
                rv_time_price.adapter = timingsAdapter
                timingsAdapter.submitList(fitnessSession.timings)
            }
        }

        override fun onClick(view: View?) {
            //launching google maps with location
            val sessionLocation = fitnessSession.location
            val gmmIntentUri = Uri.parse(
                "google.streetview:cbll=" +
                        "${sessionLocation?.latitude}," +
                        "${sessionLocation?.longitude}"
            )
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            view?.context?.startActivity(mapIntent)
        }
    }

    //diff callback for this listing
    class SessionDiffcallback : DiffUtil.ItemCallback<FitnessSession>() {

        override fun areItemsTheSame(oldItem: FitnessSession, newItem: FitnessSession): Boolean {
            return oldItem.displayAddress == newItem.displayAddress
        }

        override fun areContentsTheSame(oldItem: FitnessSession, newItem: FitnessSession): Boolean {
            return oldItem == newItem
        }
    }
}