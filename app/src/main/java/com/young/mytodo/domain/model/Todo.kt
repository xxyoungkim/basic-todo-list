package com.young.mytodo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val title: String,
    val date: Long = Calendar.getInstance().timeInMillis,
    val isDone: Boolean = false,
)