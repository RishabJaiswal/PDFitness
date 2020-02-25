package com.pune.dance.fitness.ui.profile.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pune.dance.fitness.R
import kotlinx.android.synthetic.main.item_view_profile.view.tv_profile_key
import kotlinx.android.synthetic.main.item_view_profile.view.tv_profile_value
import kotlinx.android.synthetic.main.item_view_profile_venue.view.*

class ViewProfileAdapter : ListAdapter<ProfileItem, ViewProfileAdapter.ProfileViewHolder>(ProfileDiffcallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProfileItem.VenueItem -> R.layout.item_view_profile_venue
            is ProfileItem -> R.layout.item_view_profile
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.item_view_profile_venue -> VenueViewHolder(view)
            else -> ProfileViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**this is the base view holder for every item*/
    open class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(profileItem: ProfileItem) {
            itemView.apply {
                tv_profile_key.text = profileItem.key
                tv_profile_value.text = profileItem.value
            }
        }
    }


    /**view holder containing location of the user's fitness session*/
    class VenueViewHolder(view: View) : ProfileViewHolder(view), View.OnClickListener {

        init {
            itemView.btn_get_directions.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class ProfileDiffcallback : DiffUtil.ItemCallback<ProfileItem>() {

        override fun areItemsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
            return oldItem.value == newItem.value
        }
    }
}