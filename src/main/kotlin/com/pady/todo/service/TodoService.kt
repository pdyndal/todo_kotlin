package com.pady.todo.service

import com.pady.todo.jooq.keys.TODO_U_ID_FK
import com.pady.todo.jooq.tables.records.TodoRecord
import com.pady.todo.mapper.UserMapper
import com.pady.todo.model.Todo
import com.pady.todo.repository.TodoRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TodoService(
    private val todoRepository: TodoRepository
) {
    private val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    fun existsById(todoId: Long): Boolean = todoRepository.existById(todoId)

    fun getById(todoId: Long): Todo? = todoRepository.getById(todoId)?.let {
        userMapper.toTodo(it)
    }

    fun findAll(): List<Todo> = todoRepository.findAll()
        .map {
            userMapper.toTodo(it)
        }

    @Transactional
    fun save(todo: Todo): Todo {
        var todoRecord: TodoRecord = userMapper.toTodoRecord(todo)
        todoRecord = todoRepository.upsert(todoRecord)

        return userMapper.toTodo(todoRecord)
    }

    fun delete(userId: Long) = todoRepository.deleteById(userId)
}