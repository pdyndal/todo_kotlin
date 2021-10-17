package com.pady.todo.model

import java.util.*

data class Todo(val id: UUID, val title: String, val content: String? = "")