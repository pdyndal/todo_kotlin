package com.pady.todo.mapper

import com.pady.todo.jooq.tables.records.TodoRecord
import com.pady.todo.jooq.tables.records.UserRecord
import com.pady.todo.model.Todo
import com.pady.todo.model.User

import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface UserMapper {
    @Mapping(target = "id", source = "userRecord.UId")
    fun toUser(userRecord: UserRecord, todos: List<TodoRecord>): User

    @Mapping(target = "UId", source = "id")
    fun toUserRecord(user: User): UserRecord

    @Mapping(target = "id", source = "TId")
    fun toTodo(todoRecord: TodoRecord): Todo

    @InheritInverseConfiguration
    fun toTodosRecord(todo: Todo): TodoRecord

    fun toTodosRecords(todos: List<Todo>): List<TodoRecord>
}