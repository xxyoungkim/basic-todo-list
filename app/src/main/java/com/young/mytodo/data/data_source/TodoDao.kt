package com.young.mytodo.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.young.mytodo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    // DAO (Data Access Object)
    // Room 데이터베이스에서 데이터를 직접 조작(읽기, 쓰기)하는 역할을 하는 컴포넌트

    @Query("SELECT * FROM Todo ORDER BY date DESC")
    fun observe(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 동일한 데이터 insert 시 대체
    suspend fun insert(entity: Todo)

    @Update
    suspend fun update(entity: Todo)

    @Delete
    suspend fun delete(entity: Todo)
}