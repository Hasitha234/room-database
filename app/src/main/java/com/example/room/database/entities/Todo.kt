package com.example.room.database.entities

import androidx.room.PrimaryKey

data class Todo(
    var item: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

