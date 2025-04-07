package com.facts.service

import java.security.MessageDigest
import java.util.Base64
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UrlShortenerService {

    fun generateShortUrl(input: String, length: Int = 8): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())

        val encoded = Base64.getUrlEncoder().encodeToString(digest)
        return encoded.replace("=", "").take(length)
    }
}
