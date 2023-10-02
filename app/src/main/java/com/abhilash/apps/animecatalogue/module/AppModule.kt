package com.abhilash.apps.animecatalogue.module

import android.content.Context
import com.abhilash.apps.animecatalogue.model.Repository
import com.abhilash.apps.animecatalogue.model.datastore.DataStoreManager
import com.abhilash.apps.animecatalogue.model.usecase.GetAccessTokenImpl
import com.abhilash.apps.animecatalogue.model.usecase.SetAuthToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesRepository(): Repository = Repository()

    @Singleton
    @Provides
    fun providesDataStoreManager(@ApplicationContext context: Context): DataStoreManager = DataStoreManager(context)

    @Singleton
    @Provides
    fun providesAuthToken(repository: Repository): SetAuthToken = GetAccessTokenImpl(repository)
}