package com.facts.controller

import com.facts.service.FactService
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Admin API", description = "Administrative operations")
class AdminController @Inject constructor(
    private val factService: FactService
) {

    @GET
    @Path("/statistics")
    @Operation(summary = "Get statistics", description = "Returns statistics about facts in the system")
    fun getStatistics(): Response {
        val statistics = factService.getStatistics()
        return Response.ok(statistics).build()
    }
}
