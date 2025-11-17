package com.young.mytodo.data.repository

import com.young.mytodo.data.data_source.TodoDao
import com.young.mytodo.domain.model.Todo
import com.young.mytodo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomTodoRepository @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {

    override fun searchTodos(query: String): Flow<List<Todo>> {
        return dao.search(query)
    }

    override fun observeTodos(): Flow<List<Todo>> {
        return dao.observe()
    }

    override suspend fun addTodo(todo: Todo) {
        return dao.insert(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        return dao.update(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        return dao.delete(todo)
    }
}