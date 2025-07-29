package com.young.mytodo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val title: String,
    val date: Long = System.currentTimeMillis(),
    val isDone: Boolean = false,
)