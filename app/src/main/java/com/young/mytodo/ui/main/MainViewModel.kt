package com.young.mytodo.ui.main

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.young.mytodo.domain.model.Todo
import com.young.mytodo.domain.repository.TodoRepository
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val todoRepository: TodoRepository,
) : AndroidViewModel(application = application) {
    // ViewModel의 역할: UI 상태 관리 및 사용자 인터랙션에 따른 비즈니스 로직 처리

    private val _items = mutableStateOf(emptyList<Todo>())
    val items: State<List<Todo>> = _items

    private var recentlyDeleteTodo: Todo? = null

    private val _editingTodo = mutableStateOf<Todo?>(null)
    val editingTodo: State<Todo?> = _editingTodo

    init {
        viewModelScope.launch {
            todoRepository.observeTodos()
                .collect { todos ->
                    _items.value = todos
                }
        }
    }

    fun addTodo(text: String) {
        viewModelScope.launch {
            todoRepository.addTodo(Todo(title = text))
        }
    }

    fun deleteTodo(index: Int) {
        val todo = _items.value.find { todo ->
            todo.uid == index
        }
        todo?.let {
            viewModelScope.launch {
                todoRepository.deleteTodo(it)
                recentlyDeleteTodo = it
            }
        }
    }

    // 삭제한 todo 복구
    fun restoreTodo() {
        // return@launch은 launch 코루틴 블록 안에서 벗어난다는 의미
        viewModelScope.launch {
            todoRepository.addTodo(recentlyDeleteTodo ?: return@launch) // ?: null 처리 elvis 연산자
            recentlyDeleteTodo = null
        }
    }

    // 완료/미완료 toggle
    fun toggle(index: Int) {
        val todo = _items.value.find { todo ->
            todo.uid == index
        }
        todo?.let {
            viewModelScope.launch {
                todoRepository.updateTodo(
                    it.copy(isDone = !it.isDone)
                )
            }
        }
    }

    fun startEditing(index: Int) {
        val todo = _items.value.find { it.uid == index }
        _editingTodo.value = todo
    }

    fun updateTodo(text: String) {
        _editingTodo.value?.let { original ->
            viewModelScope.launch {
                todoRepository.updateTodo(original.copy(title = text))
                _editingTodo.value = null // 수정 완료 후 초기화
            }
        }
    }

    fun cancelEditing() {
        _editingTodo.value = null
    }
}