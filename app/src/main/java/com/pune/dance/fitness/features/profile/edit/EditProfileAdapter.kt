package com.pune.dance.fitness.features.profile.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class EditProfileAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EditNameFragment()
            1 -> EditTImePlaceFragment()
            2 -> EditProfileDoneFragment()
            else -> Fragment()
        }
    }
}