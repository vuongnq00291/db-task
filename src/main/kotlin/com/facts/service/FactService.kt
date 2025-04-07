package com.facts.service

import com.facts.client.UselessFactsClient
import com.facts.exception.NotFoundException
import com.facts.dto.ExternalFact
import com.facts.dto.FactAccessStatsDto
import com.facts.dto.ShortenedFactDto
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class FactService @Inject constructor(
    @RestClient private val factsClient: UselessFactsClient,
    private val urlShortenerService: UrlShortenerService,
    private val cacheService: CacheService,
    @ConfigProperty(name = "facts.default-language", defaultValue = "en")
    private val defaultLanguage: String
) {
    private val logger = LoggerFactory.getLogger(FactService::class.java)


    /**
     * Fetches a random fact, shortens its URL, and caches it.
     */
    fun fetchAndShortenFact(): ShortenedFactDto {
        return runCatching {
            val fact = factsClient.getRandomFact(defaultLanguage)
            val shortenedUrl = urlShortenerService.generateShortUrl(fact.permalink)
            cacheService.cacheFact(shortenedUrl, fact)

            ShortenedFactDto(
                originalFact = fact.text,
                shortenedUrl = shortenedUrl
            )
        }.onFailure { exception ->
            logger.error("Failed to fetch and shorten fact", exception)
        }.getOrThrow() // Re-throw for global handler
    }

    /**
     * Retrieves a cached fact by its shortened URL and increments access count.
     */
    fun getFact(shortenedUrl: String, incrementAccessCount: Boolean = true): ExternalFact {
        val fact = cacheService.getFact(shortenedUrl)
            ?: throw NotFoundException("Fact with shortened URL '$shortenedUrl' not found")

        if (incrementAccessCount) {
            cacheService.incrementAccessCount(shortenedUrl)
        }
        return fact
    }

    /**
     * Retrieves all cached facts.
     */
    fun getAllFacts(): List<ExternalFact> {
        return cacheService.getAllFacts().values.toList()
    }
    /**
     * Gets access statistics for all shortened URLs sorted by count.
     */
    fun getStatistics(): List<FactAccessStatsDto> {
        return cacheService.getAllStatistics().map { (shortenedUrl, accessCount) ->
            FactAccessStatsDto(shortenedUrl, accessCount)
        }
    }
}
