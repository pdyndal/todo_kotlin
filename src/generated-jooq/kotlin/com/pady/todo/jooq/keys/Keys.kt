/*
 * This file is generated by jOOQ.
 */
package com.pady.todo.jooq.keys


import com.pady.todo.jooq.tables.Todo
import com.pady.todo.jooq.tables.User
import com.pady.todo.jooq.tables.records.TodoRecord
import com.pady.todo.jooq.tables.records.UserRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val ADDRESS_T_ID_PK: UniqueKey<TodoRecord> = Internal.createUniqueKey(Todo.TODO, DSL.name("ADDRESS_T_ID_PK"), arrayOf(Todo.TODO.T_ID), true)
val USER_U_ID_PK: UniqueKey<UserRecord> = Internal.createUniqueKey(User.USER, DSL.name("USER_U_ID_PK"), arrayOf(User.USER.U_ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val ADDRESS_U_ID_FK: ForeignKey<TodoRecord, UserRecord> = Internal.createForeignKey(Todo.TODO, DSL.name("ADDRESS_U_ID_FK"), arrayOf(Todo.TODO.U_ID), com.pady.todo.jooq.keys.USER_U_ID_PK, arrayOf(User.USER.U_ID), true)
