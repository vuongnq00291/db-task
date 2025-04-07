package com.facts.controller

import com.facts.dto.FactDetailDto
import com.facts.dto.toDto
import com.facts.service.FactService
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/facts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Facts", description = "Fact operations")
class FactController @Inject constructor(
    private val factService: FactService
) {

    @POST
    @Operation(
        summary = "Create a shortened fact",
        description = "Fetches a fact from an external source and shortens it"
    )
    @APIResponse(responseCode = "200", description = "Successfully created a shortened fact")
    fun createFact(): Response {
        val shortenedFact = factService.fetchAndShortenFact()
        return Response.ok(shortenedFact).build()
    }

    @GET
    @Path("/{shortenedUrl}")
    @Operation(
        summary = "Get a fact by shortened URL",
        description = "Returns the fact associated with the provided shortened URL"
    )
    @APIResponse(responseCode = "200", description = "Successfully retrieved the fact")
    @APIResponse(responseCode = "404", description = "Fact not found")
    fun getFact(
        @Parameter(description = "Shortened URL of the fact")
        @PathParam("shortenedUrl") shortenedUrl: String
    ): Response {
        val fact = factService.getFact(shortenedUrl)
        return Response.ok(fact.toDto()).build()
    }

    @GET
    @Operation(summary = "Get all facts", description = "Returns all facts stored in the system")
    @APIResponse(responseCode = "200", description = "Successfully retrieved all facts")
    fun getAllFacts(): Response {
        val facts = factService.getAllFacts().map { fact -> fact.toDto()}
        return Response.ok(facts).build()
    }

    @GET
    @Path("/{shortenedUrl}/redirect")
    @Operation(summary = "Redirect to original fact", description = "Redirects to the original permalink of the fact")
    @APIResponse(responseCode = "307", description = "Temporary redirect to the original permalink")
    @APIResponse(responseCode = "404", description = "Fact not found")
    fun redirectToOriginalFact(
        @Parameter(description = "Shortened URL of the fact")
        @PathParam("shortenedUrl") shortenedUrl: String
    ): Response {
        val fact = factService.getFact(shortenedUrl)
        return Response.temporaryRedirect(java.net.URI(fact.permalink)).build()
    }
}
