package kroonprins.mocker

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.util.concurrent.TimeUnit

class MockServer(val port: Int, val ruleProvider: () -> List<Rule>) {

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
                routing.route(rule.request.path, rule.request.method) {
                    handle {
                        call.respondText(
                            contentType = ContentType.parse(rule.response!!.contentType),
                            status = HttpStatusCode.fromValue(Integer.parseInt(rule.response!!.statusCode))
                        )
                        { rule.response!!.body ?: "" }
                    }
                }
            }
    }


}