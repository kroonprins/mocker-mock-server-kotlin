package kroonprins.mocker.templating

import com.fasterxml.jackson.annotation.JsonCreator
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.ApplicationRequest
import kroonprins.mocker.*
import kroonprins.mocker.kroonprins.mocker.templating.TemplatedRequest
import kroonprins.mocker.kroonprins.mocker.templating.TemplatedResponse
import kroonprins.mocker.kroonprins.mocker.templating.TemplatedRule
import kroonprins.mocker.templating.jinjava.createJinjavaTemplatingContext
import kroonprins.mocker.templating.jinjava.templateWithJinjava

sealed class TemplatingEngine(
    val templatingContextCreator: (ApplicationRequest) -> Any?,
    val templatingFunction: (String?, Any?) -> String?
) {
    object None : TemplatingEngine({ null }, { x, _ -> x })
    object Jinjava : TemplatingEngine(::createJinjavaTemplatingContext, ::templateWithJinjava)

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromString(str: String): TemplatingEngine {
            return when (str.toLowerCase()) {
                "jinjava" -> Jinjava
                else -> None
            }
        }
    }
}

fun template(rule: Rule, templatingContext: Any?): TemplatedRule {
    return TemplatedRule(
        name = rule.name,
        request = TemplatedRequest(
            path = rule.request.path,
            method = HttpMethod.parse(rule.request.method.toUpperCase())
        ),
        response = templateResponse(rule, templatingContext)
    )
}

fun templateResponse(rule: Rule, templatingContext: Any?): TemplatedResponse {
    if (rule.response != null) {
        return templateResponse(rule.response, templatingContext)
    } else {
        return templateResponse(rule.conditionalResponse!!, templatingContext)
    }
}

fun templateResponse(response: Response, templatingContext: Any?): TemplatedResponse {
    return templateTemplatableResponse(response, templatingContext, response.templatingEngine.templatingFunction)
}

fun templateResponse(conditionalResponse: ConditionalResponse, templatingContext: Any?): TemplatedResponse {
    val conditionalResponseValue = conditionalResponse.response.first { it.condition == "true" }
    return templateTemplatableResponse(
        conditionalResponseValue,
        templatingContext,
        conditionalResponse.templatingEngine.templatingFunction
    )
}

fun templateTemplatableResponse(
    response: TemplatableResponse,
    templatingContext: Any?,
    templateFunction: (String?, Any?) -> String?
): TemplatedResponse {
    return TemplatedResponse(
        contentType = ContentType.parseNullable(response.contentType()),
        statusCode = HttpStatusCode.fromValue(Integer.parseInt(response.statusCode())),
        body = templateFunction(response.body(), templatingContext)
    )
}
