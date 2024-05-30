package com.example.room.database.repositories

import com.example.room.database.TodoDatabase
import com.example.room.database.entities.Todo

class TodoRepository(
    private val db:TodoDatabase
){
    suspend fun insert(todo: Todo) = db.getTodoDao().insertTodo(todo)
    suspend fun delete(todo:Todo) = db.getTodoDao().deleteTodo(todo)
    fun getAllTodoItems():List<Todo> = db.getTodoDao().getAllTodoItems()
}