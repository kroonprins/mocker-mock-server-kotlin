package kroonprins.mocker

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.shyiko.klob.Glob
import java.nio.file.Files
import java.nio.file.Paths

val mapper: ObjectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun provideRules(): Sequence<Rule> {
    return Glob.from("rules/*.yaml")
        .iterate(Paths.get("."))
        .asSequence()
        .map { path -> Files.readString(path) }
        .map { str -> mapper.readValue<Rule>(str) }
}