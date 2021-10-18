package com.pady.todo.resource

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter
import com.pady.todo.model.Todo
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TodoResourceTest {

    private lateinit var defaultSpec: RequestSpecification
    private var currentTodo: Todo? = null

    @BeforeEach
    fun setUp(@LocalServerPort localServerPort: Int) {
        defaultSpec = Given {
            basePath("/todos")
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
    fun `Should return no todos`() {
        Given {
            spec(defaultSpec)
        } When {
            get()
        } Then {
            statusCode(200)
            body("$.size()", Matchers.equalTo(0))
        }
    }

    @Order(20)
    @Test
    fun `Should create todo`() {
        val bodyTodo = Todo(null, "Test")

        Given {
            spec(defaultSpec)
            body(bodyTodo)
        } When {
            post()
        } Then {
            statusCode(201)
        } Extract {
            val expectedTodo = bodyTodo.copy()

            val responseTodo = body().`as`(Todo::class.java)
            assertEquals(expectedTodo, responseTodo)
            currentTodo = responseTodo
        }
    }

    @Order(30)
    @Test
    fun `Should return todo by id`() {

        Assumptions.assumeTrue(currentTodo != null)
        Assumptions.assumeTrue(currentTodo?.id != null)

        Given {
            spec(defaultSpec)
        } When {
            get("/{todoId}", currentTodo?.id)
        } Then {
            statusCode(200)
        } Extract {
            val responseTodo = body().`as`(Todo::class.java)
            assertEquals(currentTodo, responseTodo)
        }
    }

    @Order(31)
    @Test
    fun `Should return list with one todo`() {
        Given {
            spec(defaultSpec)
        } When {
            get()
        } Then {
            statusCode(200)
            body("$.size()", Matchers.equalTo(0))
        } Extract {
            val responseUser = body().jsonPath().getList("$", Todo::class.java)[0]
            assertEquals(currentTodo, responseUser)
        }
    }

    @Order(40)
    @Test
    fun `Should modify todo`() {

        Assumptions.assumeTrue(currentTodo != null)
        Assumptions.assumeTrue(currentTodo?.id != null)

        val bodyTodo = currentTodo!!.copy( currentTodo?.id, "Test 2", "Test content")

        Given {
            spec(defaultSpec)
            body(bodyTodo)
        } When {
            put("/{todoId}", currentTodo?.id)
        } Then {
            statusCode(200)
        } Extract {
            val expectedTodo = bodyTodo.copy()

            val responseTodo = body().`as`(Todo::class.java)
            assertEquals(expectedTodo, responseTodo)
            currentTodo = responseTodo
        }
    }

    @Order(41)
    @Test
    fun `Should return 404 when modifying non existing todo`() {

        val bodyUser = Todo(null, "Test note")

        Given {
            spec(defaultSpec)
            body(bodyUser)
        } When {
            put("/{todoId}", UUID.randomUUID())
        } Then {
            statusCode(404)
        }
    }

    @Order(50)
    @Test
    fun `Should get todo by id after modification`() {
        Assumptions.assumeTrue(currentTodo != null)
        Assumptions.assumeTrue(currentTodo?.id != null)

        Given {
            spec(defaultSpec)
        } When {
            get("/{todoId}", currentTodo?.id)
        } Then {
            statusCode(200)
        } Extract {
            val responseUser = body().`as`(Todo::class.java)
            assertEquals(currentTodo, responseUser)
        }
    }

    @Order(60)
    @Test
    fun `Should delete todo`() {

        Assumptions.assumeTrue(currentTodo != null)
        Assumptions.assumeTrue(currentTodo?.id != null)

        Given {
            spec(defaultSpec)
        } When {
            delete("/{todoId}", currentTodo?.id)
        } Then {
            statusCode(204)
        }
    }

    @Order(61)
    @Test
    fun `Should return 404 when deleting non existing todo`() {
        Given {
            spec(defaultSpec)
        } When {
            delete("/{todoId}", UUID.randomUUID())
        } Then {
            statusCode(204)
        }
    }

    @Order(70)
    @Test
    fun `Should return no todos after deletion`() {
        Given {
            spec(defaultSpec)
        } When {
            get()
        } Then {
            statusCode(200)
            body("$.size()", Matchers.equalTo(0))
        }
    }

    @Order(71)
    @Test
    fun `Should get 404 for not existing todo`() {
        Given {
            spec(defaultSpec)
        } When {
            get("/{todoId}", UUID.randomUUID())
        } Then {
            statusCode(404)
        }
    }
}