package com.pune.dance.fitness.ui.profile.view

open class ProfileItem(
    var key: String = "",
    var value: String = ""
) {


    data class VenueItem(
        var latitude: String,
        var longitude: String
    ) : ProfileItem()
}