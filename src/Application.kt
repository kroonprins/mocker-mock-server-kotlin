package kroonprins.mocker

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {

    val job = launch {
        val mockServer = MockServer(8080, ::provideRules)
        mockServer.start()
    }

    job.join()
}

