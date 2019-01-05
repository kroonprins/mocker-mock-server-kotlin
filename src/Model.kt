package kroonprins.mocker

import io.ktor.http.HttpMethod

data class Request(
    val path: String,
    val method: HttpMethod
)

data class Response(
    val contentType: String,
    val statusCode: String,
    val body: String?
)

data class ConditionalResponse(
    val response: List<ConditionalResponseValue>
)

data class ConditionalResponseValue(
    val contentType: String,
    val statusCode: String,
    val body: String?
)

data class Rule(
    val name: String,
    val request: Request,
    val response: Response? = null,
    val conditionalResponse: ConditionalResponse? = null
)