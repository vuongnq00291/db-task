package com.facts.client.exception

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper
import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.MultivaluedMap

@Provider
@ApplicationScoped
class UselessFactsExceptionMapper : ResponseExceptionMapper<UselessFactsClientException> {

    override fun toThrowable(response: Response): UselessFactsClientException {
        val statusCode = response.status
        val errorMessage = try {
            response.readEntity(String::class.java)
        } catch (e: Exception) {
            "Unable to read error message from response"
        }

        return when (statusCode) {
            Response.Status.NOT_FOUND.statusCode ->
                UselessFactsClientException("Resource not found: $errorMessage", statusCode)
            Response.Status.SERVICE_UNAVAILABLE.statusCode ->
                UselessFactsClientException("External service unavailable: $errorMessage", statusCode)
            else ->
                UselessFactsClientException("External API error (status code $statusCode): $errorMessage", statusCode)
        }
    }

    override fun handles(status: Int, headers: MultivaluedMap<String, Any>?): Boolean {
        return status >= 400
    }
}
