package com.young.mytodo.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.young.mytodo.domain.model.Todo

@Database(entities = [Todo::class], version = 2)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    // Hilt가 인스턴스 생성과 관리 담당
}