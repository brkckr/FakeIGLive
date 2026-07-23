package com.brkckr.fakeiglive.data.datasource

import com.brkckr.fakeiglive.domain.model.Comment

interface LiveDataSource {
    fun getComments(): List<Comment>
    fun getViewerRange(): IntRange
    fun getViewerChangeRange(): IntRange
    fun getTrendChangeProbability(): Float
    fun getCommentDelayRange(): LongRange
    fun getViewerDelayRange(): LongRange
    fun getHeartDelayRange(): LongRange
}
