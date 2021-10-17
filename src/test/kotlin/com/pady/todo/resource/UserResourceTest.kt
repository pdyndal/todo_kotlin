package com.pady.todo.resource

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
internal class UserResourceTest {
    private lateinit var defaultSpecification: RequestSpecification

    @BeforeAll
    fun beforeAll(@LocalServerPort localServerPort: Int) {
        defaultSpecification = Given {
            basePath("/users")
            port(localServerPort)
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            filter(
                OpenApiValidationFilter("todo.yaml")
            )
        }
    }

    @Order(10)
    @Test
    fun `Should return users`() {
        Given {
            spec(defaultSpecification)
        } When {
            get()
        } Then {
            statusCode(200)
            body("$.size()", equalTo(1))
            body("[0].name", equalTo("Karol"))
            log().body()
        }
    }
}