package kroonprins.mocker.templating.jinjava

import com.hubspot.jinjava.Jinjava
import io.ktor.request.ApplicationRequest
import io.ktor.request.path

val jinjava = Jinjava()

fun createJinjavaTemplatingContext(request: ApplicationRequest): Map<String, Any> {
    return mapOf<String, Any>(
        "req" to mapOf<String, Any>(
            "path" to request.path()
        ),
        "res" to ""
    )
}

fun templateWithJinjava(toTemplate: String?, context: Any?): String? {
    if (toTemplate == null) return null
    return jinjava.render(toTemplate, context as? Map<String, Any>)
}