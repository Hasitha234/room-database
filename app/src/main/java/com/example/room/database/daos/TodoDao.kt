package com.example.room.database.daos

import android.content.Context
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.room.database.entities.Todo

interface TodoDao {
    @Insert
    suspend fun insertTodo(todo: Todo)
    @Delete
    suspend fun deleteTodo(todo: Todo)
    @Query("SELECT * FROM Todo")
    fun getAllTodoItems():List<Todo>

    @Database(entities = [Todo::class], version = 1)
    abstract class TodoDatabase: RoomDatabase() {
        abstract fun getTodoDao(): TodoDao

        companion object {
            @Volatile
            private var INSTANCE: TodoDatabase? = null
            fun getInstance(context: Context): TodoDatabase {
                synchronized(this) {
                    return INSTANCE ?: Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_db"
                    ).build().also {
                        INSTANCE = it
                    }
                }
            }
        }
    }
}