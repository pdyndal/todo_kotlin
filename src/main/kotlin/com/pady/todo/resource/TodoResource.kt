package com.pady.todo.resource

import com.pady.todo.model.Todo
import com.pady.todo.service.TodoService
import com.pady.todo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(path = ["/todos"])
@RestController
class TodoResource(private val todoService: TodoService) {

    @GetMapping()
    fun findTodos(): ResponseEntity<List<Todo>> = ResponseEntity(todoService.findAll(), HttpStatus.OK)

    @PostMapping()
    fun newTodo(@RequestBody todo: Todo): ResponseEntity<Todo> =
        ResponseEntity(todoService.save(todo), HttpStatus.CREATED)

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<Todo> =
        todoService.getById(todoId)
            ?.let { ResponseEntity(it, HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @PutMapping("/{todoId}")
    fun updateTodo(@PathVariable todoId: Long, @RequestBody todo: Todo): ResponseEntity<Todo> =
        if (todoService.existsById(todoId))
            ResponseEntity(
                todoService.save(todo.copy(id = todoId)),
                HttpStatus.OK
            ) else ResponseEntity(HttpStatus.NOT_FOUND)

    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable todoId: Long): ResponseEntity<Todo> =
        if (todoService.existsById(todoId)) {
            todoService.delete(todoId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else ResponseEntity(HttpStatus.NOT_FOUND)
}