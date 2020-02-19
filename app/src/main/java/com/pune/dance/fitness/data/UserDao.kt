package com.pune.dance.fitness.data

import com.pune.dance.fitness.api.user.models.User
import com.pune.dance.fitness.api.user.models.UserFields

class UserDao : BaseDao() {

    fun getUser(userId: String): User? {
        return realm.where(User::class.java)
            .equalTo(UserFields.ID, userId)
            .findFirst()
    }

}