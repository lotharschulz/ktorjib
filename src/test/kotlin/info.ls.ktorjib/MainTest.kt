package info.ls.ktorjib

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class MainTest {
    @Test
    fun testRoot() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hi", response.content)
            }
        }
    }
}
