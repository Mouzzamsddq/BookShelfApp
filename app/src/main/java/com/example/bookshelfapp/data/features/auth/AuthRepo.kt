package com.example.bookshelfapp.data.features.auth

import com.example.bookshelfapp.data.features.auth.repository.local.AuthLocalDataSource
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
) {
    suspend fun performSignup(users: Users) = authLocalDataSource.performSignUp(users = users)
}