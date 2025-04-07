package com.facts.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class FactDetailDto(
    val fact: String,

    @JsonProperty("original_permalink")
    val originalPermalink: String
)
