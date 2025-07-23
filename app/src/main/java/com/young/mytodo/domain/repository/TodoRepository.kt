package com.young.mytodo.domain.repository

import com.young.mytodo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    // 데이터 소스(예: DAO)와 ViewModel 사이에서 데이터 접근 로직을 추상화하고 관리하는 MVVM 레이어 - Repository

    fun observeTodos(): Flow<List<Todo>>

    suspend fun addTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)
}