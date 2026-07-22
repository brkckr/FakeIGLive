package com.brkckr.fakeiglive.domain.repository

import com.brkckr.fakeiglive.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

// manage local persistence of user settings and profile
interface UserPreferences {
    fun getUserProfile(): Flow<UserProfile?> // retrieve saved profile information
    suspend fun saveUserProfile(profile: UserProfile) // persist profile information locally
}
