package com.young.mytodo.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.young.mytodo.domain.model.Todo

@Database(entities = [Todo::class], version = 2)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        // Migration 정의
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 인덱스 추가
                database.execSQL("CREATE INDEX IF NOT EXISTS index_Todo_date ON Todo(date)")
            }
        }

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo-db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}