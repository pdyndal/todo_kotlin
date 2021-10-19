package com.pady.todo.model

data class User(val id: Long?, val name: String, val surname: String, val todos: List<Todo>)
