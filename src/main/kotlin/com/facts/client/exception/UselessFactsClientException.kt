package com.facts.client.exception

class UselessFactsClientException(message: String, val statusCode: Int) : RuntimeException(message)
