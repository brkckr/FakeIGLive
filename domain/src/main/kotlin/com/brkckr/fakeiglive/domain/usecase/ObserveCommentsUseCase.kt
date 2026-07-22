package com.brkckr.fakeiglive.domain.usecase

import com.brkckr.fakeiglive.domain.model.Comment
import com.brkckr.fakeiglive.domain.model.DomainResult
import com.brkckr.fakeiglive.domain.repository.LiveRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// fetch and observe mock comment flow
class ObserveCommentsUseCase @Inject constructor(
    private val repository: LiveRepository,
) {
    operator fun invoke(): Flow<DomainResult<Comment>> = repository.observeComments()
}
