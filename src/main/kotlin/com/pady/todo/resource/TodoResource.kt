package com.pady.todo.resource

import com.pady.todo.model.Todo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(path = ["/todos"])
@RestController
class TodoResource {

    @GetMapping()
    fun findTodos(): ResponseEntity<List<Todo>> {
        throw NotImplementedError()
    }

    @PostMapping()
    fun newTodo(@RequestBody todo: Todo): ResponseEntity<Todo> {
        throw NotImplementedError()
    }

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: UUID): ResponseEntity<Todo> {
        throw NotImplementedError()
    }

    @PutMapping("/{todoId}")
    fun updateTodo(@PathVariable todoId: UUID, @RequestBody todo: Todo): ResponseEntity<Todo> {
        throw NotImplementedError()
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable todoId: UUID): ResponseEntity<Todo> {
        throw NotImplementedError()
    }
}