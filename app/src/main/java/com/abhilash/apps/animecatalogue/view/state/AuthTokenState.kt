package com.abhilash.apps.animecatalogue.view.state


sealed class AuthTokenState {
    object Idle: AuthTokenState()
    object Loading: AuthTokenState()
    class Success(authToken: String): AuthTokenState()
    object Failure: AuthTokenState()
}
