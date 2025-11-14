package com.young.mytodo.ui.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.young.mytodo.domain.model.Todo
import com.young.mytodo.domain.repository.TodoRepository
import com.young.mytodo.util.MediaStoreFileManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainViewModel(
    application: Application,
    private val todoRepository: TodoRepository,
) : AndroidViewModel(application = application) {
    // ViewModel의 역할: UI 상태 관리 및 사용자 인터랙션에 따른 비즈니스 로직 처리

    // MediaStore 파일 매니저로 변경
    private val mediaStoreFileManager = MediaStoreFileManager(application)

    // todo 내보내기 상태 관리
    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState: StateFlow<ExportState> = _exportState.asStateFlow()

    // 최근 삭제된 todo
    private var recentlyDeleteTodo: Todo? = null

    // 수정 중인 todo
    private val _editingTodo = mutableStateOf<Todo?>(null)
    val editingTodo: State<Todo?> = _editingTodo

    // 검색어
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 초기화 상태
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()
    
    // 전체 데이터
    private val allTodosFlow: Flow<List<Todo>> = todoRepository.observeTodos()

    // 필터링된 데이터 변환
    private val filteredTodosFlow: Flow<List<Todo>> = combine(
        allTodosFlow,
        _searchQuery
    ) { todos, query ->
        if (query.isBlank()) {
            todos
        } else {
            todos.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

    val groupedItems: StateFlow<Map<String, List<Todo>>> = filteredTodosFlow
        .map { todos ->
            todos
                .groupBy { dateFormatHeader(it.date) }
                .toSortedMap(reverseOrder()) // 날짜 기준 역순 정렬
        }
        .onEach {
            if(!_isInitialized.value) {
                _isInitialized.value = true
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val items: StateFlow<List<Todo>> = filteredTodosFlow
        .onEach { todos ->
            Log.d("ViewModel", "Items size: ${todos.size}")
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // 파일 내보내기용 아이템 목록
    val groupedItemsToDownloads: StateFlow<Map<String, List<Todo>>> = allTodosFlow
        .map { todos ->
            todos
                .groupBy { dateFormatHeader(it.date) }
                .toSortedMap(reverseOrder())
        }
        .onEach {
            if(!_isInitialized.value) {
                _isInitialized.value = true
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    init {
        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            allTodosFlow.first() // 첫 번째 데이터 대기
            val endTime = System.currentTimeMillis()
            Log.d("Performance", "Data loading 걸리는 시간: ${endTime - startTime}ms")
        }
    }

    fun addTodo(text: String) {
        viewModelScope.launch {
            todoRepository.addTodo(Todo(title = text))
        }
    }

    fun deleteTodo(todoId: Int) {
        val todo = items.value.find { it.uid == todoId }
        todo?.let {
            viewModelScope.launch {
                todoRepository.deleteTodo(it)
                recentlyDeleteTodo = it
            }
        }
    }

    /**
     * 삭제한 todo 복구
     */
    fun restoreTodo() {
        // return@launch은 launch 코루틴 블록 안에서 벗어난다는 의미
        viewModelScope.launch {
            todoRepository.addTodo(recentlyDeleteTodo ?: return@launch) // ?: null 처리 elvis 연산자
            recentlyDeleteTodo = null
        }
    }

    /**
     * 완료/미완료 toggle
     */
    fun toggle(todoId: Int) {
        val todo = items.value.find { it.uid == todoId }

        if (todo == null) {
            Log.w("Toggle", "Todo not found with id: $todoId")
            return
        }

        viewModelScope.launch {
            try {
                todoRepository.updateTodo(
                    todo.copy(isDone = !todo.isDone)
                )
            } catch (e: Exception) {
                Log.e("Toggle", "Failed to toggle todo: ${e.message}")
            }
        }
    }

    /**
     * (디버깅) toggle + 로그
     */
    fun toggleWithLogging(todoId: Int) {
        println("Toggle 요청 item todoId: $todoId")
        println("현재 items: ${items.value.map { "${it.uid}: ${it.title}" }}")

        val todo = items.value.find { it.uid == todoId }
        println("찾은 todo: $todo")

        toggle(todoId)
    }

    /**
     * (디버깅) 현재 상태 확인용
     */
    fun debugCurrentState() {
        viewModelScope.launch {
            val currentItems = items.value
            println("=== 현재 ViewModel 상태 ===")
            println("전체 items 개수: ${currentItems.size}")
            currentItems.forEach { todo ->
                println("- id: ${todo.uid}, title: ${todo.title}, isDone: ${todo.isDone}")
            }
            println("=========================")
        }
    }

    fun startEditing(todoId: Int) {
        val todo = items.value.find { it.uid == todoId }
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

    /**
     * 검색
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }


    private fun dateFormatHeader(date: Long): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26 이상
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val instant = Instant.ofEpochMilli(date)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            return formatter.format(localDateTime)
        } else {
            // API 26 미만
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatter.format(Date(date))
        }
    }

    /**
     * todo 목록을 Downloads 폴더에 텍스트 파일로 저장
     */
    fun exportTodosToDownloads() {
        viewModelScope.launch {
            try {
                _exportState.value = ExportState.Loading

                // 현재 모든 할 일 목록 가져오기
                val currentGroupedTodos = groupedItemsToDownloads.value

                if (currentGroupedTodos.isEmpty()) {
                    _exportState.value = ExportState.Error("내보낼 할 일이 없습니다.")
                    return@launch
                }

                // MediaStore를 사용하여 Downloads 폴더에 파일 저장
                val result = mediaStoreFileManager.saveToDownloads(currentGroupedTodos)

                result.fold(
                    onSuccess = { filePath ->
                        _exportState.value = ExportState.Success(filePath)
                    },
                    onFailure = { exception ->
                        _exportState.value = ExportState.Error(
                            when {
                                exception.message?.contains("권한") == true ->
                                    "파일 저장 권한이 필요합니다."
                                exception.message?.contains("space") == true ->
                                    "저장 공간이 부족합니다."
                                else ->
                                    "파일 저장 중 오류가 발생했습니다: ${exception.message}"
                            }
                        )
                    }
                )

            } catch (e: Exception) {
                _exportState.value = ExportState.Error("예상치 못한 오류가 발생했습니다: ${e.message}")
            }
        }
    }

    /**
     * 내보내기 상태 초기화
     */
    fun clearExportState() {
        _exportState.value = ExportState.Idle
    }
}

/**
 * 파일 내보내기 상태 관리
 */
sealed class ExportState {
    object Idle : ExportState()
    object Loading : ExportState()
    data class Success(val filePath: String) : ExportState()
    data class Error(val message: String) : ExportState()
}