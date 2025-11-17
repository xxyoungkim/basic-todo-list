package com.young.mytodo.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.young.mytodo.data.data_source.TodoDao
import com.young.mytodo.data.data_source.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 데이터베이스 관련 의존성을 제공하는 Hilt Module
 * @InstallIn(SingletonComponent::class) - 앱의 생명주기 동안 단일 인스턴스로 유지
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * TodoDatabase 인스턴스를 제공
     * @Singleton - 앱 전체에서 단 하나의 인스턴스만 생성
     * @Provides - 이 함수가 의존성을 제공한다는 표시
     */
    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context,
    ): TodoDatabase {
        // Migration 정의
        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 인덱스 추가
                database.execSQL("CREATE INDEX IF NOT EXISTS index_Todo_date ON Todo(date)")
            }
        }

        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo-db"
        )
            .addMigrations(migration1to2)
            .build()
    }

    /**
     * TodoDao 인스턴스를 제공
     * Database가 이미 주입되므로 database.todoDao()를 반환
     */
    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }
}