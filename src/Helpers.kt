package kroonprins.mocker

import io.ktor.http.ContentType

fun ContentType.Companion.parseNullable(value: String?): ContentType? {
    if (value == null) {
        return null
    }
    return ContentType.parse(value)
}