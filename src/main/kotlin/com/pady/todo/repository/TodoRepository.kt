package com.pady.todo.repository

import asBytes
import com.pady.todo.jooq.tables.records.TodoRecord
import com.pady.todo.jooq.tables.references.TODO
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TodoRepository(private val dslContext: DSLContext) {
    fun existById(todoId: Long): Boolean =
        dslContext.fetchExists(dslContext.selectFrom(TODO).where(TODO.T_ID.eq(todoId)))

    fun getById(todoId: Long): TodoRecord = dslContext.selectFrom(TODO).where(TODO.T_ID.eq(todoId)).fetchSingle()

    fun findAll(): List<TodoRecord> = dslContext.selectFrom(TODO).fetch()

    fun upsert(todoRecord: TodoRecord): TodoRecord {
        val insertValues = todoRecord.intoMap()
        val updateValues = todoRecord.intoMap().minus(TODO.T_ID.name)

        return dslContext.insertInto(TODO).set(insertValues)
            .onDuplicateKeyUpdate().set(updateValues)
            .returning()
            .fetchSingle()
    }

    fun upsert(todoRecord: List<TodoRecord>) {
        val queries = todoRecord.map {
            val insertValues = it.intoMap()
            val updateValues = it.intoMap().minus(TODO.T_ID.name)

            dslContext.insertInto(TODO).set(insertValues)
                .onDuplicateKeyUpdate().set(updateValues)
        }

        dslContext.batch(queries).execute()
    }

    fun deleteAllTodosByIdNotIn(ids: List<Long>) {
        dslContext.deleteFrom(TODO).where(TODO.T_ID.notIn(ids)).execute()
    }

    fun deleteById(id: Long) = dslContext.delete(TODO).where(TODO.T_ID.eq(id)).execute()
}