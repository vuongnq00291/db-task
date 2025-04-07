package com.facts.dto

data class ExternalFact(
    val text: String,
    val permalink: String
)

fun ExternalFact.toDto():FactDetailDto =
    FactDetailDto(
    fact = this.text,
    originalPermalink = this.permalink
)