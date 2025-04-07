package com.facts.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortenedFactDto(
    @JsonProperty("original_fact")
    val originalFact: String,
    @JsonProperty("shortened_url")
    val shortenedUrl: String
)
