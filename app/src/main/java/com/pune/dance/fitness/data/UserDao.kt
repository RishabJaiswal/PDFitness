package com.pune.dance.fitness.data

import com.pune.dance.fitness.api.user.models.User
import com.pune.dance.fitness.api.user.models.UserProfile
import com.pune.dance.fitness.api.user.models.UserProfileFields

class UserDao : BaseDao() {

    fun getUser(): User? {
        return realm.where(User::class.java)
            .findFirst()
    }

    fun getUserProfile(userId: String): UserProfile? {
        return realm.where(UserProfile::class.java)
            .equalTo(UserProfileFields.USER_ID, userId)
            .findFirst()
    }

}