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

    @GetMapping()
    fun findUsers(): ResponseEntity<List<User>> {
        throw NotImplementedError()
    }

    @PostMapping()
    fun newUser(@RequestBody user: User): ResponseEntity<User> {
        throw NotImplementedError()
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID): ResponseEntity<User> {
        throw NotImplementedError()
    }

    @PutMapping("/{userId}")
    fun updateUser(@PathVariable userId: UUID, @RequestBody user: User): ResponseEntity<User> {
        throw NotImplementedError()
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: UUID): ResponseEntity<User> {
        throw NotImplementedError()
    }
}