package kroonprins.mocker.kroonprins.mocker.templating

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

data class TemplatedRequest(
    val path: String,
    val method: HttpMethod
)

data class TemplatedResponse(
    val contentType: ContentType? = null,
    val statusCode: HttpStatusCode,
    val body: String? = null
)

data class TemplatedRule(
    val name: String,
    val request: TemplatedRequest, // TODO - can be just Request once figured out how to get Request class to use HttpMethod instead of String for method
    val response: TemplatedResponse
)

//data class TemplateContext (
//    val req: TemplateContextRequest
//)
//
//data class TemplateContextRequest(
//    val
//)