package com.brkckr.fakeiglive.data

import com.brkckr.fakeiglive.data.datasource.MockLiveDataSourceImpl
import com.brkckr.fakeiglive.data.repository.LiveRepositoryImpl
import com.brkckr.fakeiglive.domain.model.DomainResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LiveRepositoryImplTest {
    private val repository = LiveRepositoryImpl(dataSource = MockLiveDataSourceImpl())

    @Test
    fun `viewer stream starts inside the supported range`() = runTest {
        val result = repository.observeViewerCount().first()
        assertTrue(result is DomainResult.Success)
        val viewerCount = (result as DomainResult.Success).data

        assertTrue(viewerCount in 10_000..100_000)
    }

    @Test
    fun `viewer stream fluctuates without leaving its range`() = runTest {
        val viewerCounts = repository.observeViewerCount()
            .take(20)
            .map { (it as DomainResult.Success).data }
            .toList()

        assertTrue(viewerCounts.all { it in 10_000..100_000 })
        assertTrue(viewerCounts.zipWithNext().any { (previous, current) -> previous != current })
    }

    @Test
    fun `comment stream emits a populated comment`() = runTest {
        val result = repository.observeComments().first()
        assertTrue(result is DomainResult.Success)
        val comment = (result as DomainResult.Success).data

        assertTrue(comment.username.isNotBlank())
        assertTrue(comment.text.isNotBlank())
        assertTrue(comment.profileImageUri.isNotBlank())
    }

    @Test
    fun `comment stream does not repeat the same comment consecutively`() = runTest {
        val comments = repository.observeComments()
            .take(2)
            .map { (it as DomainResult.Success).data }
            .toList()

        assertNotEquals(comments[0], comments[1])
    }

    @Test
    fun `heart stream produces a burst`() = runTest {
        val result = repository.observeHearts().first()
        assertTrue(result is DomainResult.Success)
    }
}
