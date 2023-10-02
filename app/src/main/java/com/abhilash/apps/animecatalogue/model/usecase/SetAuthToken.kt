package com.abhilash.apps.animecatalogue.model.usecase

import com.abhilash.apps.animecatalogue.model.Repository
import java.security.AuthProvider
import javax.inject.Inject
import javax.inject.Singleton

interface SetAuthToken {
    fun setAuthToken(authToken: String?)
}

@Singleton
class GetAccessTokenImpl @Inject constructor(
    private val repository: Repository
): SetAuthToken {
    override fun setAuthToken(authToken: String?) {
        repository.updateAuthToken(authToken)
    }
}