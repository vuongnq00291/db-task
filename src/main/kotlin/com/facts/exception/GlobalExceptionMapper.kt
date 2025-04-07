package com.facts.exception

import com.facts.client.exception.UselessFactsClientException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class GlobalExceptionMapper : ExceptionMapper<Throwable> {

    override fun toResponse(exception: Throwable): Response {
        return when (exception) {
            is UselessFactsClientException -> handleUselessFactsClientException(exception)
            is IllegalArgumentException -> Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponse("Bad Request", exception.message ?: "Invalid request parameters"))
                .build()
            is NotFoundException -> Response
                .status(Response.Status.NOT_FOUND)
                .entity(ErrorResponse("Not Found", exception.message ?: "Resource not found"))
                .build()
            else -> {
                // Log the unexpected exception here
                Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse("Internal Server Error", "An unexpected error occurred"))
                    .build()
            }
        }
    }

    private fun handleUselessFactsClientException(exception: UselessFactsClientException): Response {
        val status = when (exception.statusCode) {
            Response.Status.NOT_FOUND.statusCode -> Response.Status.NOT_FOUND
            Response.Status.SERVICE_UNAVAILABLE.statusCode -> Response.Status.SERVICE_UNAVAILABLE
            else -> Response.Status.INTERNAL_SERVER_ERROR
        }

        return Response
            .status(status)
            .entity(ErrorResponse("External API Error", exception.message ?: "Error communicating with external service"))
            .build()
    }
}
