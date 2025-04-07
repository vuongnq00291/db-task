package com.facts.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class FactAccessStatsDto(
    @JsonProperty("shortened_url")
    val shortenedUrl: String,
    @JsonProperty("access_count")
    val accessCount: Int
)
