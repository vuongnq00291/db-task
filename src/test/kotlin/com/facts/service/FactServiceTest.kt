package com.facts.service

import com.facts.client.UselessFactsClient
import com.facts.exception.NotFoundException
import com.facts.dto.ExternalFact
import com.facts.dto.ShortenedFactDto
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FactServiceTest {

    @MockK
    @RestClient
    private lateinit var factsClient: UselessFactsClient

    @MockK
    private lateinit var urlShortenerService: UrlShortenerService

    @MockK
    private lateinit var cacheService: CacheService

    private lateinit var factService: FactService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        factService = FactService(factsClient, urlShortenerService, cacheService,"en")
    }

    @Test
    fun `test fetchAndShortenFact success`() {
        // Given
        val originalFact = ExternalFact("This is a test fact", "http://example.com/test")
        val shortenedUrl = "abc123"

        every { factsClient.getRandomFact("en") } returns originalFact
        every { urlShortenerService.generateShortUrl(originalFact.permalink) } returns shortenedUrl
        every { cacheService.cacheFact(shortenedUrl, originalFact) } just Runs

        // When
        val result = factService.fetchAndShortenFact()

        // Then
        assertEquals(ShortenedFactDto(originalFact.text, shortenedUrl), result)
        verify { cacheService.cacheFact(shortenedUrl, originalFact) }
    }

    @Test
    fun `test fetchAndShortenFact handles exceptions`() {
        // Given
        every { factsClient.getRandomFact("en") } throws RuntimeException("API error")

        // When/Then
        assertThrows<RuntimeException> { factService.fetchAndShortenFact() }
    }

    @Test
    fun `test getFact success`() {
        // Given
        val shortenedUrl = "abc123"
        val fact = ExternalFact("Test fact", "http://example.com/test")

        every { cacheService.getFact(shortenedUrl) } returns fact
        every { cacheService.incrementAccessCount(shortenedUrl) } just Runs

        // When
        val result = factService.getFact(shortenedUrl)

        // Then
        assertEquals(fact, result)
        verify { cacheService.incrementAccessCount(shortenedUrl) }
    }

    @Test
    fun `test getFact not incrementing access count`() {
        // Given
        val shortenedUrl = "abc123"
        val fact = ExternalFact("Test fact", "http://example.com/test")

        every { cacheService.getFact(shortenedUrl) } returns fact

        // When
        val result = factService.getFact(shortenedUrl, false)

        // Then
        assertEquals(fact, result)
        verify(exactly = 0) { cacheService.incrementAccessCount(any()) }
    }

    @Test
    fun `test getFact throws NotFoundException for nonexistent URL`() {
        // Given
        val shortenedUrl = "nonexistent"

        every { cacheService.getFact(shortenedUrl) } returns null

        // When/Then
        assertThrows<NotFoundException> {
            factService.getFact(shortenedUrl)
        }
    }

    @Test
    fun `test getAllFacts`() {
        // Given
        val factsMap = mapOf(
            "url1" to ExternalFact("Fact 1", "http://example.com/1"),
            "url2" to ExternalFact("Fact 2", "http://example.com/2")
        )

        every { cacheService.getAllFacts() } returns factsMap

        // When
        val result = factService.getAllFacts()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.contains(factsMap["url1"]))
        assertTrue(result.contains(factsMap["url2"]))
    }

    @Test
    fun `test getStatistics`() {
        // Given
        val statsMap = mapOf(
            "url1" to 2,
            "url2" to 1
        )

        every { cacheService.getAllStatistics() } returns statsMap

        // When
        val result = factService.getStatistics()

        // Then
        assertEquals(2, result.size)
        val statsForUrl1 = result.find { it.shortenedUrl == "url1" }
        val statsForUrl2 = result.find { it.shortenedUrl == "url2" }

        assertNotNull(statsForUrl1)
        assertNotNull(statsForUrl2)
        assertEquals(2, statsForUrl1?.accessCount)
        assertEquals(1, statsForUrl2?.accessCount)
    }
}
