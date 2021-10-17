package com.pady.todo.model

import java.util.*

data class User(val id: UUID, val name: String, val surname: String, val todos: List<Todo>)
