package com.brkckr.fakeiglive.domain.usecase

import com.brkckr.fakeiglive.domain.model.DomainResult
import com.brkckr.fakeiglive.domain.repository.LiveRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// observe real-time heart burst events
class ObserveHeartsUseCase @Inject constructor(
    private val repository: LiveRepository,
) {
    operator fun invoke(): Flow<DomainResult<Unit>> = repository.observeHearts()
}
