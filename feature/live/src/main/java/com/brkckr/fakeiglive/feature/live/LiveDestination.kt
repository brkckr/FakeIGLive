package com.brkckr.fakeiglive.feature.live

import kotlinx.serialization.Serializable

@Serializable
data class LiveDestination(
    val username: String,
    val profileImageUri: String,
)
