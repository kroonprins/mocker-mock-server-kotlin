package kroonprins.mocker

import io.ktor.application.call
import io.ktor.http.Cookie
import io.ktor.http.HttpMethod
import io.ktor.request.httpMethod
import io.ktor.request.path
import io.ktor.response.header
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import kroonprins.mocker.templating.template
import mu.KotlinLogging
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

class MockServer(val port: Int, val ruleProvider: () -> Sequence<Rule>) {

    private lateinit var server: NettyApplicationEngine

    fun start() {
        server = embeddedServer(Netty, port = port) {
            routing {
                createRouting(this)
            }
        }.start()
    }

    fun stop() {
        server.stop(5L, 5L, TimeUnit.SECONDS)
    }

    private fun createRouting(routing: Routing) {
        ruleProvider()
            .forEach { rule ->
                logger.debug { "Setting up rule $rule" }
                routing.route(rule.request.path, HttpMethod.parse(rule.request.method.toUpperCase())) {
                    handle {
                        logger.info { "received request ${call.request.httpMethod.value} ${call.request.path()}. Handle with rule '${rule.name}'" }

                        val templatingContext = rule.templatingEngine().templatingContextCreator(call.request)
                        val templatedRule = template(rule, templatingContext)

                        logger.debug { "sending response for $rule" }

                        call.response.header("X-test", "testje")
                        call.response.cookies.append(Cookie("cookie1", "c1"))

                        call.respondText(
                            contentType = templatedRule.response.contentType,
                            status = templatedRule.response.statusCode
                        )
                        { templatedRule.response.body ?: "" } // TODO how to handle empty body?
                    }
                }
            }
    }


}