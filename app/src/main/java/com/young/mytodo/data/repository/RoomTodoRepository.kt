package com.young.mytodo.data.repository

import android.app.Application
import androidx.room.Room
import com.young.mytodo.data.data_source.TodoDatabase
import com.young.mytodo.domain.model.Todo
import com.young.mytodo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class RoomTodoRepository(application: Application) : TodoRepository {
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java,
        "todo-db"
    ).build()

    override fun searchTodos(query: String): Flow<List<Todo>> {
        return db.todoDao().search(query)
    }

    override fun observeTodos(): Flow<List<Todo>> {
        return db.todoDao().observe()
    }

    override suspend fun addTodo(todo: Todo) {
        return db.todoDao().insert(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        return db.todoDao().update(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        return db.todoDao().delete(todo)
    }
}