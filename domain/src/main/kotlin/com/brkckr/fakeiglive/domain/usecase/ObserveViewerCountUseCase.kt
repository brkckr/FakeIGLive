package com.brkckr.fakeiglive.domain.usecase

import com.brkckr.fakeiglive.domain.model.DomainResult
import com.brkckr.fakeiglive.domain.repository.LiveRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// observe fluctuating viewer count stream
class ObserveViewerCountUseCase @Inject constructor(
    private val repository: LiveRepository,
) {
    operator fun invoke(): Flow<DomainResult<Int>> = repository.observeViewerCount()
}
