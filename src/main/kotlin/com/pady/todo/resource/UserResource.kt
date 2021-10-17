package com.pady.todo.resource

import com.pady.todo.model.Todo
import com.pady.todo.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(path = ["/users"])
@RestController
class UserResource {

    @GetMapping
    fun findUsers(): ResponseEntity<List<User>> {
        return ResponseEntity(
            listOf(User(UUID.randomUUID(), "Karol", "Nowak",
                listOf(Todo(UUID.randomUUID(), "Test")))),
            HttpStatus.OK
        )
    }
}