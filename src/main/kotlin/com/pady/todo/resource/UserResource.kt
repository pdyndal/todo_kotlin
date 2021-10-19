package com.pady.todo.resource

import com.pady.todo.model.Todo
import com.pady.todo.model.User
import com.pady.todo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(path = ["/users"])
@RestController
class UserResource(private val userService: UserService) {

    @GetMapping()
    fun findUsers(): ResponseEntity<List<User>> = ResponseEntity(userService.findAll(), HttpStatus.OK)

    @PostMapping()
    fun newUser(@RequestBody user: User): ResponseEntity<User> =
        ResponseEntity(userService.save(user), HttpStatus.CREATED)

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): ResponseEntity<User> =
        userService.getById(userId)
            ?.let { ResponseEntity(it, HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @PutMapping("/{userId}")
    fun updateUser(@PathVariable userId: Long, @RequestBody user: User): ResponseEntity<User> =
        if (userService.existsById(userId))
            ResponseEntity(
                userService.save(user.copy(id = userId)),
                HttpStatus.OK
            ) else ResponseEntity(HttpStatus.NOT_FOUND)

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<User> =
        if (userService.existsById(userId)) {
            userService.delete(userId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else ResponseEntity(HttpStatus.NOT_FOUND)
}