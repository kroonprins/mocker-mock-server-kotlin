package kroonprins.mocker

import io.ktor.http.HttpMethod

fun provideDummy(): List<Rule> {
    return listOf(
        Rule(
            name = "rule 1",
            request = Request("/get1", HttpMethod.Get),
            response = Response(
                contentType = "text/plain",
                statusCode = "200",
                body = "body rule 1"
            )
        ),
        Rule(
            name = "rule 2",
            request = Request("/get2", HttpMethod.Get),
            response = Response(
                contentType = "application/json",
                statusCode = "200",
                body = """
                    {
                        "rule": "2"
                    }
                """
            )
        )
    )
}

//val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
//
//val str = "x"
//
//val r: Rule = mapper.readValue(str, Rule.)
//
//fun provideRules() {
//    return Glob.from("rules/*.yaml")
//        .iterate(Paths.get("."))
//        .asSequence()
//        .map { path -> Files.readString(path) }
//        .map { str -> mapper.readValue(str, Rule.class) }
//}