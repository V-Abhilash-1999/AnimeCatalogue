package com.abhilash.apps.animecatalogue.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhilash.apps.animecatalogue.AppUtils.collectLatestNonNull
import com.abhilash.apps.animecatalogue.model.datastore.DataStoreManager
import com.abhilash.apps.animecatalogue.model.datastore.LoginMode
import com.abhilash.apps.animecatalogue.model.usecase.SetAuthToken
import com.abhilash.apps.animecatalogue.view.state.AuthTokenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: DataStoreManager,
    private val setAuthToken: SetAuthToken
): ViewModel() {
    private val _viewState: MutableState<AuthTokenState> = mutableStateOf(AuthTokenState.Idle)
    val viewState: State<AuthTokenState> = _viewState

    val loginMode = dataStore.getLoginMethod()

    init {
        viewModelScope.launch {
            collectAuthToken()
        }
    }

    private suspend fun collectAuthToken() {
        dataStore
            .getAuthToken()
            .collectLatestNonNull { token ->
                _viewState.value = AuthTokenState.Success(token)
            }
    }

    fun setLoginMode(loginMode: LoginMode) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLoginMethod(loginMode)
        }
    }

    fun setAuthToken(token: String) {
        setLoginMode(LoginMode.USER_LOGIN)
        setAuthToken.setAuthToken(token)
        saveAuthToken(token)
        setAuthTokenState(AuthTokenState.Success(token))
    }

    private fun saveAuthToken(token: String) {
        viewModelScope.launch {
            dataStore.saveAuthToken(token)
        }
    }

    fun authTokenSetFailure() {
        if(viewState.value == AuthTokenState.Loading) {
            setAuthTokenState(AuthTokenState.Failure)
            setAuthToken.setAuthToken(null)
        }
    }

    private fun setAuthTokenState(state: AuthTokenState) {
        viewModelScope.launch {
            _viewState.value = state
        }
    }

}