package kroonprins.mocker

import kroonprins.mocker.templating.TemplatingEngine

data class Request(
    val path: String,
//TODO    val method: HttpMethod
    val method: String
)

data class Response(
    val templatingEngine: TemplatingEngine,
    val contentType: String? = null,
    val statusCode: String,
    val body: String? = null
) : TemplatableResponse {
    override fun contentType(): String? {
        return contentType
    }

    override fun statusCode(): String {
        return statusCode
    }

    override fun body(): String? {
        return body
    }
}

data class ConditionalResponse(
    val templatingEngine: TemplatingEngine,
    val response: List<ConditionalResponseValue>
)

data class ConditionalResponseValue(
    val condition: String,
    val contentType: String? = null,
    val statusCode: String,
    val body: String? = null
) : TemplatableResponse {
    override fun contentType(): String? {
        return contentType
    }

    override fun statusCode(): String {
        return statusCode
    }

    override fun body(): String? {
        return body
    }
}

data class Rule(
    val name: String,
    val request: Request,
    val response: Response? = null,
    val conditionalResponse: ConditionalResponse? = null
) {
    fun templatingEngine(): TemplatingEngine {
        if (response != null) {
            return response.templatingEngine
        } else {
            return conditionalResponse!!.templatingEngine
        }
    }
}

// TODO find more elegant way
interface TemplatableResponse {
    fun contentType(): String?
    fun statusCode(): String
    fun body(): String?
}

//@JvmStatic
//@JsonCreator
//fun HttpMethod.Companion.jsonCreator(value: String): HttpMethod {
//    return HttpMethod.parse(value.toUpperCase())
//}