package com.brkckr.fakeiglive.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brkckr.fakeiglive.domain.model.UserProfile
import com.brkckr.fakeiglive.domain.repository.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// implement profile persistence using jetpack datastore
@Singleton
class UserPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences> // instance provided via hilt
) : UserPreferences {

    override fun getUserProfile(): Flow<UserProfile?> = dataStore.data.map { preferences ->
        val username = preferences[KEY_USERNAME]
        val profileImageUri = preferences[KEY_PROFILE_IMAGE_URI]
        
        if (username != null && profileImageUri != null) {
            UserProfile(username = username, profileImageUri = profileImageUri)
        } else null
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[KEY_USERNAME] = profile.username
            preferences[KEY_PROFILE_IMAGE_URI] = profile.profileImageUri
        }
    }

    private companion object {
        val KEY_USERNAME = stringPreferencesKey("username") // key for username storage
        val KEY_PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri") // key for image path storage
    }
}
