package com.facts.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UrlShortenerServiceTest {

    private val urlShortenerService = UrlShortenerService()

    @Test
    fun `test generated URL is correct length`() {
        val url = "http://example.com/very/long/url/that/needs/shortening"
        val shortUrl = urlShortenerService.generateShortUrl(url, 8)
        assertEquals(8, shortUrl.length)
    }

    @Test
    fun `test same input produces same output`() {
        val url = "http://example.com/test"
        val shortUrl1 = urlShortenerService.generateShortUrl(url)
        val shortUrl2 = urlShortenerService.generateShortUrl(url)
        assertEquals(shortUrl1, shortUrl2)
    }

    @Test
    fun `test different inputs produce different outputs`() {
        val url1 = "http://example.com/test1"
        val url2 = "http://example.com/test2"
        val shortUrl1 = urlShortenerService.generateShortUrl(url1)
        val shortUrl2 = urlShortenerService.generateShortUrl(url2)
        assertNotEquals(shortUrl1, shortUrl2)
    }

    @Test
    fun `test custom length`() {
        val url = "http://example.com/test"
        val shortUrl4 = urlShortenerService.generateShortUrl(url, 4)
        val shortUrl12 = urlShortenerService.generateShortUrl(url, 12)
        assertEquals(4, shortUrl4.length)
        assertEquals(12, shortUrl12.length)
    }
}
