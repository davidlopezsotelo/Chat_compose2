package com.dls.chatcompose2.domain.repository

import com.dls.chatcompose2.domain.model.User

interface UserRepository {
    suspend fun getAllUsers(): Result<List<User>>
}



