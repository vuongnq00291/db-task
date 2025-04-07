package com.facts.client

import com.facts.client.exception.UselessFactsExceptionMapper
import com.facts.dto.ExternalFact
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam

@RegisterRestClient(configKey = "useless-facts-api")
@RegisterProvider(UselessFactsExceptionMapper::class)
interface UselessFactsClient {
    @GET
    @Path("/facts/random")
    fun getRandomFact(@QueryParam("language") language: String): ExternalFact
}
