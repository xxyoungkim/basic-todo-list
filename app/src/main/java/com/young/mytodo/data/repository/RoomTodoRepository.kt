package com.young.mytodo.data.repository

import android.app.Application
import com.young.mytodo.data.data_source.TodoDatabase
import com.young.mytodo.domain.model.Todo
import com.young.mytodo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class RoomTodoRepository(application: Application) : TodoRepository {
    private val db = TodoDatabase.getDatabase(application)
    private val dao = db.todoDao()

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