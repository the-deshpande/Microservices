package com.example

import com.example.models.UserRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testHelloWorld() = testApplication {
        environment {
            config = MapApplicationConfig(
                "couch.username" to "admin",
                "couch.password" to "1234",
                "jwt.secret" to "top-secret"
            )
        }
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testGetAll() = testApplication {
        environment {
            config = MapApplicationConfig(
                "couch.username" to "admin",
                "couch.password" to "1234",
                "jwt.secret" to "top-secret"
            )
        }
        application {
            module()
        }

        client.get("/user/get-all").apply { assertEquals(HttpStatusCode.OK, status) }
    }

    @Test
    fun testLogin() = testApplication {
        environment {
            config = MapApplicationConfig(
                "couch.username" to "admin",
                "couch.password" to "1234",
                "jwt.secret" to "top-secret"
            )
        }
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/user/login"){
            contentType(ContentType.Application.Json)
            setBody(UserRequest("John Doe", "1234"))
        }.apply { assertEquals(HttpStatusCode.Accepted, status) }
    }

    @Test
    fun testRegister() = testApplication {
        environment {
            config = MapApplicationConfig(
                "couch.username" to "admin",
                "couch.password" to "1234",
                "jwt.secret" to "top-secret"
            )
        }
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/user/register"){
            contentType(ContentType.Application.Json)
            setBody(UserRequest("New User", "1234"))
        }.apply { assertEquals(HttpStatusCode.Created, status) }
    }

    @Test
    fun testDelete() = testApplication {
        environment {
            config = MapApplicationConfig(
                "couch.username" to "admin",
                "couch.password" to "1234",
                "jwt.secret" to "top-secret"
            )
        }
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/user/login"){
            contentType(ContentType.Application.Json)
            setBody(UserRequest("New User", "1234"))
        }.apply { assertEquals(HttpStatusCode.Accepted, status) }

        client.delete("/user/delete"){
            header("Authorization", "Bearer ${response.bodyAsText()}")
        }.apply { assertEquals(HttpStatusCode.OK, status) }
    }

    @Test
    fun testSecret() = testApplication {
        environment {
            config = MapApplicationConfig(
                "couch.username" to "admin",
                "couch.password" to "1234",
                "jwt.secret" to "top-secret"
            )
        }
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/user/login"){
            contentType(ContentType.Application.Json)
            setBody(UserRequest("John Doe", "1234"))
        }.apply { assertEquals(HttpStatusCode.Accepted, status) }

        client.get("/user/secret"){
            header("Authorization", "Bearer ${response.bodyAsText()}")
        }.apply { assertEquals(HttpStatusCode.Accepted, status) }
    }
}
