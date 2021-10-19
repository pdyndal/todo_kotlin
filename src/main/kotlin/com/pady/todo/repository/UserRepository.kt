package com.pady.todo.repository

import asBytes
import com.pady.todo.jooq.tables.records.TodoRecord
import com.pady.todo.jooq.tables.records.UserRecord
import com.pady.todo.jooq.tables.references.USER
import com.pady.todo.jooq.tables.references.TODO
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(private val dslContext: DSLContext) {

    fun existById(userId: Long): Boolean =
        dslContext.fetchExists(dslContext.selectFrom(USER).where(USER.U_ID.eq(userId)))

    fun getById(userId: Long): UserRecord = dslContext.selectFrom(USER).where(USER.U_ID.eq(userId)).fetchSingle()

    fun findAll(): List<UserRecord> = dslContext.selectFrom(USER).fetch()

    fun upsert(userRecord: UserRecord): UserRecord {
        val insertValues = userRecord.intoMap()
        val updateValues = userRecord.intoMap().minus(USER.U_ID.name)

        return dslContext.insertInto(USER).set(insertValues)
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