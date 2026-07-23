package com.brkckr.fakeiglive.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.brkckr.fakeiglive.data.datasource.LiveDataSource
import com.brkckr.fakeiglive.data.datasource.MockLiveDataSourceImpl
import com.brkckr.fakeiglive.data.repository.LiveRepositoryImpl
import com.brkckr.fakeiglive.data.repository.UserPreferencesImpl
import com.brkckr.fakeiglive.domain.repository.LiveRepository
import com.brkckr.fakeiglive.domain.repository.UserPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

// configure hilt bindings for repository and data source
@Module
@InstallIn(SingletonComponent::class)
abstract class LiveModule {

    @Binds
    @Singleton
    abstract fun bindLiveDataSource(
        impl: MockLiveDataSourceImpl,
    ): LiveDataSource

    @Binds
    @Singleton
    abstract fun bindLiveRepository(
        impl: LiveRepositoryImpl,
    ): LiveRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferences(
        impl: UserPreferencesImpl,
    ): UserPreferences

    companion object {
        @Provides
        @Singleton
        fun provideDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> = context.dataStore
    }
}
