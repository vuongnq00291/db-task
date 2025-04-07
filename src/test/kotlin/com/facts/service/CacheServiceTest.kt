package com.facts.service

import com.facts.dto.ExternalFact
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CacheServiceTest {

    private lateinit var cacheService: CacheService

    @BeforeEach
    fun setUp() {
        cacheService = CacheService()
    }

    @Test
    fun `test cache and retrieve fact`() {
        // Given
        val shortenedUrl = "abc123"
        val fact = ExternalFact("Test fact", "http://example.com/test")

        // When
        cacheService.cacheFact(shortenedUrl, fact)
        val retrievedFact = cacheService.getFact(shortenedUrl)

        // Then
        assertEquals(fact, retrievedFact)
    }

    @Test
    fun `test get non-existent fact returns null`() {
        // When
        val result = cacheService.getFact("nonexistent")

        // Then
        assertNull(result)
    }

    @Test
    fun `test get all facts`() {
        // Given
        val fact1 = ExternalFact("Fact 1", "http://example.com/1")
        val fact2 = ExternalFact("Fact 2", "http://example.com/2")

        // When
        cacheService.cacheFact("url1", fact1)
        cacheService.cacheFact("url2", fact2)
        val allFacts = cacheService.getAllFacts()

        // Then
        assertEquals(2, allFacts.size)
        assertEquals(fact1, allFacts["url1"])
        assertEquals(fact2, allFacts["url2"])
    }

    @Test
    fun `test access count increments correctly`() {
        // Given
        val shortenedUrl = "abc123"
        val fact = ExternalFact("Test fact", "http://example.com/test")

        // When
        cacheService.cacheFact(shortenedUrl, fact)

        // Then
        assertEquals(0, cacheService.getAccessCount(shortenedUrl))

        // When increment multiple times
        cacheService.incrementAccessCount(shortenedUrl)
        cacheService.incrementAccessCount(shortenedUrl)
        cacheService.incrementAccessCount(shortenedUrl)

        // Then
        assertEquals(3, cacheService.getAccessCount(shortenedUrl))
    }

    @Test
    fun `test get access count for non-existent URL returns zero`() {
        // When
        val count = cacheService.getAccessCount("nonexistent")

        // Then
        assertEquals(0, count)
    }

    @Test
    fun `test get all statistics`() {
        // Given
        val fact1 = ExternalFact("Fact 1", "http://example.com/1")
        val fact2 = ExternalFact("Fact 2", "http://example.com/2")

        // When
        cacheService.cacheFact("url1", fact1)
        cacheService.cacheFact("url2", fact2)
        cacheService.incrementAccessCount("url1")
        cacheService.incrementAccessCount("url1")
        cacheService.incrementAccessCount("url2")

        val stats = cacheService.getAllStatistics()

        // Then
        assertEquals(2, stats.size)
        assertEquals(2, stats["url1"])
        assertEquals(1, stats["url2"])
    }
}
