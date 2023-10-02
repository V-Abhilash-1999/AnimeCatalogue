package com.abhilash.apps.animecatalogue.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstants.LOGIN_MODE.name)
    private val dataStore = context.dataStore

    companion object {
        val loginMethodKey = stringPreferencesKey(DataStoreConstants.LOGIN_MODE.name)
        val authTokenKey = stringPreferencesKey(DataStoreConstants.AUTH_TOKEN.name)
    }

    suspend fun setLoginMethod(
        loginMode: LoginMode
    ) {
        dataStore.edit { pref ->
            pref[loginMethodKey] = loginMode.name
        }
    }

    suspend fun clear() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }

    fun getLoginMethod(): Flow<LoginMode>  {
         return dataStore.data
             .catch { exception ->
                 if(exception is IOException) {
                     emit(emptyPreferences())
                 } else {
                     throw  exception
                 }
             }
             .map { pref ->
                 pref[loginMethodKey]?.let { loginModeString ->
                     LoginMode.values().find { it.name == loginModeString }
                 } ?: LoginMode.UNREGISTERED
             }
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { pref ->
            pref[authTokenKey] = token
        }
    }

    fun getAuthToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if(exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw  exception
                }
            }
            .map { pref ->
                pref[authTokenKey]
            }
    }
}