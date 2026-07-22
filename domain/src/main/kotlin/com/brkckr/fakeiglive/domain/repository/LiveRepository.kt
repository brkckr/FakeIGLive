package com.brkckr.fakeiglive.domain.repository

import com.brkckr.fakeiglive.domain.model.Comment
import com.brkckr.fakeiglive.domain.model.DomainResult
import kotlinx.coroutines.flow.Flow

// define data stream contracts for the live session
interface LiveRepository {
    fun observeComments(): Flow<DomainResult<Comment>>

    fun observeViewerCount(): Flow<DomainResult<Int>>

    fun observeHearts(): Flow<DomainResult<Unit>>
}
