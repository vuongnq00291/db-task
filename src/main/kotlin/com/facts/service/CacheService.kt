package com.facts.service

import com.facts.dto.ExternalFact
import javax.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@ApplicationScoped
class CacheService {

    // Map to store facts by shortened URL
    private val factCache = ConcurrentHashMap<String, ExternalFact>()

    // Map to track access counts
    private val accessCounts = ConcurrentHashMap<String, AtomicInteger>()

    /**
     * Stores a fact with its shortened URL.
     */
    fun cacheFact(shortenedUrl: String, fact: ExternalFact) {
        factCache[shortenedUrl] = fact
        accessCounts[shortenedUrl] = AtomicInteger(0)
    }

    /**
     * Retrieves a fact by its shortened URL.
     */
    fun getFact(shortenedUrl: String): ExternalFact? {
        return factCache[shortenedUrl]
    }

    /**
     * Retrieves all cached facts.
     */
    fun getAllFacts(): Map<String, ExternalFact> {
        return factCache.toMap()
    }

    /**
     * Increments the access count for a shortened URL.
     */
    fun incrementAccessCount(shortenedUrl: String) {
        accessCounts[shortenedUrl]?.incrementAndGet()
    }

    /**
     * Gets the access count for a shortened URL.
     */
    fun getAccessCount(shortenedUrl: String): Int {
        return accessCounts[shortenedUrl]?.get() ?: 0
    }

    /**
     * Gets all access statistics.
     */
    fun getAllStatistics(): Map<String, Int> {
        return accessCounts.mapValues { it.value.get() }
    }
}
