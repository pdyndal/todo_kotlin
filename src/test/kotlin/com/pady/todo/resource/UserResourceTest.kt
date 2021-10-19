package com.pady.todo.resource

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter
import com.pady.todo.model.Todo
import com.pady.todo.model.User
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UserResourceTest {

    private lateinit var defaultSpec: RequestSpecification
    private var currentUser: User? = null

    @BeforeEach
    fun setUp(@LocalServerPort localServerPort: Int) {
        defaultSpec = Given {
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
    fun `Should return no users`() {
        Given {
            spec(defaultSpec)
        } When {
            get()
        } Then {
            statusCode(200)
            body("$.size()", equalTo(0))
        }
    }

    @Order(20)
    @Test
    fun `Should create user`() {
        val bodyUser = User(null, "Krystyna", "Kowalska", listOf(Todo(null, "Test")))

        Given {
            spec(defaultSpec)
            body(bodyUser)
        } When {
            post()
        } Then {
            statusCode(201)
        } Extract {
            val todos = listOf(bodyUser.todos[0].copy(id = 1))
            val expectedUser = bodyUser.copy(id = 1, todos = todos)

            val responseUser = body().`as`(User::class.java)
            assertEquals(expectedUser, responseUser)
            currentUser = responseUser
        }
    }

    @Order(30)
    @Test
    fun `Should return user by id`() {

        assumeTrue(currentUser != null)
        assumeTrue(currentUser?.id != null)

        Given {
            spec(defaultSpec)
        } When {
            get("/{userId}", currentUser?.id)
        } Then {
            statusCode(200)
        } Extract {
            val responseUser = body().`as`(User::class.java)
            assertEquals(currentUser, responseUser)
        }
    }

    @Order(31)
    @Test
    fun `Should return list with one user`() {
        Given {
            spec(defaultSpec)
        } When {
            get()
        } Then {
            statusCode(200)
            body("$.size()", equalTo(0))
        } Extract {
            val responseUser = body().jsonPath().getList("$", User::class.java)[0]
            assertEquals(currentUser, responseUser)
        }
    }

    @Order(40)
    @Test
    fun `Should modify user`() {

        assumeTrue(currentUser != null)
        assumeTrue(currentUser?.id != null)

        val bodyUser = currentUser!!.copy(name = "Andrzej", surname = "Brzoza", todos = listOf(Todo(null, "Test 2")))

        Given {
            spec(defaultSpec)
            body(bodyUser)
        } When {
            put("/{userId}", currentUser?.id)
        } Then {
            statusCode(200)
        } Extract {
            val todos = listOf(bodyUser.todos[0].copy(id = 2))
            val expectedUser = bodyUser.copy(id = 1, todos = todos)

            val responseUser = body().`as`(User::class.java)
            assertEquals(expectedUser, responseUser)
            currentUser = responseUser
        }
    }

    @Order(41)
    @Test
    fun `Should return 404 when modyfing non existing user`() {

        val bodyUser = User(null, "Krystyna", "Kowalska", listOf(Todo(null, "Test note")))

        Given {
            spec(defaultSpec)
            body(bodyUser)
        } When {
            put("/{userId}", 123456)
        } Then {
            statusCode(404)
        }
    }

    @Order(50)
    @Test
    fun `Should get user by id after modification`() {
        assumeTrue(currentUser != null)
        assumeTrue(currentUser?.id != null)

        Given {
            spec(defaultSpec)
        } When {
            get("/{userId}", currentUser?.id)
        } Then {
            statusCode(200)
        } Extract {
            val responseUser = body().`as`(User::class.java)
            assertEquals(currentUser, responseUser)
        }
    }

    @Order(60)
    @Test
    fun `Should delete user`() {

        assumeTrue(currentUser != null)
        assumeTrue(currentUser?.id != null)

        Given {
            spec(defaultSpec)
        } When {
            delete("/{userId}", currentUser?.id)
        } Then {
            statusCode(204)
        }
    }

    @Order(61)
    @Test
    fun `Should return 404 when deleting non existing user`() {
        Given {
            spec(defaultSpec)
        } When {
            delete("/{userId}", 123456)
        } Then {
            statusCode(204)
        }
    }

    @Order(70)
    @Test
    fun `Should return no users after deletion`() {
        Given {
            spec(defaultSpec)
        } When {
            get()
        } Then {
            statusCode(200)
            body("$.size()", equalTo(0))
        }
    }

    @Order(71)
    @Test
    fun `Should get 404 for not existing user`() {
        Given {
            spec(defaultSpec)
        } When {
            get("/{userId}", 123456)
        } Then {
            statusCode(404)
        }
    }
}