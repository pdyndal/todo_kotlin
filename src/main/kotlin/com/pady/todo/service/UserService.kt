package com.pady.todo.service

import com.pady.todo.jooq.keys.TODO_U_ID_FK
import com.pady.todo.jooq.tables.records.UserRecord
import org.mapstruct.factory.Mappers
import com.pady.todo.mapper.UserMapper
import com.pady.todo.model.User
import com.pady.todo.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {
    private val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    fun existsById(userId: Long): Boolean = userRepository.existById(userId)

    fun getById(userId: Long): User? = userRepository.getById(userId)?.let {
        val todos = it.fetchChildren(TODO_U_ID_FK)
        userMapper.toUser(it, todos)
    }

    fun findAll(): List<User> = userRepository.findAll()
        .map {
            val todos = it.fetchChildren(TODO_U_ID_FK)
            userMapper.toUser(it, todos)
        }

    @Transactional
    fun save(user: User): User {
        var userRecord: UserRecord = userMapper.toUserRecord(user)
        userRecord = userRepository.upsert(userRecord)

        var todosRecords = userMapper.toTodoRecords(user.todos)
        todosRecords.mapNotNull { it.tId }.let { userRepository.deleteAllTodosByIdNotIn(it) }

        todosRecords.forEach { it.uId = userRecord.uId }
        userRepository.upsert(todosRecords)

        todosRecords = userRecord.fetchChildren(TODO_U_ID_FK)
        return userMapper.toUser(userRecord, todosRecords)
    }

    fun delete(userId: Long) = userRepository.deleteById(userId)
}