package com.pune.dance.fitness.ui.profile.view

sealed class ProfileItem(
    val key: String = "",
    val value: String = ""
) {


    data class VenueItem(
        val latitude: String,
        val longitude: String
    ) : ProfileItem()
}