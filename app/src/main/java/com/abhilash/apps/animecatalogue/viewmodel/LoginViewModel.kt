package com.abhilash.apps.animecatalogue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.abhilash.apps.animecatalogue.model.Repository
import com.abhilash.apps.animecatalogue.model.datastore.DataStoreManager
import com.abhilash.apps.animecatalogue.model.datastore.LoginMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: DataStoreManager,
    private val repository: Repository,
): ViewModel() {
    val getLoginMode = dataStore.getLoginMethod()

    fun setLoginMode(loginMode: LoginMode) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLoginMethod(loginMode)
        }
    }

}