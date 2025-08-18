package com.young.mytodo.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.young.mytodo.R
import com.young.mytodo.ui.main.components.TodoItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }

    val editingTodo = viewModel.editingTodo.value
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var editingTodoId by rememberSaveable { mutableStateOf<Int?>(null) }

    val searchQuery by viewModel.searchQuery
    var isSearching by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(editingTodo) {
        editingTodo?.let {
            text = it.title // 수정 모드면 기존 텍스트 입력
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                viewModel.updateSearchQuery(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("검색어를 입력해 주세요.", color = Color.LightGray) },
                            singleLine = true,
                        )
                    } else {
                        Text("I CAN DO IT")
                    }
                },
                actions = {
                    if (isSearching) {
                        IconButton(onClick = {
                            isSearching = false
                            viewModel.updateSearchQuery("") // 검색 종료 시 전체 목록 복원
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "닫기",
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            isSearching = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "검색",
                            )
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        // 수정 취소 BackHandler
        BackHandler(enabled = editingTodo != null) {
            // 뒤로가기 눌렀을 때 실행할 로직
            text = ""
            viewModel.cancelEditing()
            editingTodoId = null
            isEditing = false
            focusManager.clearFocus()
        }
        // 검색 취소 BackHandler
        BackHandler(enabled = isSearching) {
            // 뒤로가기 눌렀을 때 실행할 로직
            isSearching = false
            viewModel.updateSearchQuery("")
            focusManager.clearFocus()
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                maxLines = 1,
                placeholder = { Text("할 일을 입력해 주세요.", color = Color.LightGray) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                if (editingTodo != null) {
                                    viewModel.updateTodo(text)
                                    editingTodoId = null
                                } else {
                                    viewModel.addTodo(text)
                                }
                                text = ""
                                focusManager.clearFocus() // 키보드 숨기기
                            }
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // 키보드 옵션-확인 버튼 체크 모양
                keyboardActions = KeyboardActions(onDone = {
                    if (editingTodo != null) {
                        viewModel.updateTodo(text)
                        editingTodoId = null
                    } else {
                        viewModel.addTodo(text)
                    }
                    text = ""
                    focusManager.clearFocus() // 키보드 숨기기
                }),
            )

            Divider()

            LazyColumn(
//                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            ) {
                itemsIndexed(viewModel.items.value) { index, todoItem ->
                    val isEditingItem = (todoItem.uid == editingTodoId)

                    Column {
                        TodoItem(
                            todo = todoItem,
                            onClick = { index ->
                                viewModel.toggle(index)
                            },
                            onDeleteClick = { index ->
                                viewModel.deleteTodo(index)

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "할 일이 삭제되었습니다.",
                                        actionLabel = "취소",
                                        duration = SnackbarDuration.Short,
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.restoreTodo()
                                    }
                                }
                            },
                            onUpdateClick = { index ->
                                isEditing = true
                                editingTodoId = index
                                viewModel.startEditing(index)
                            },
                            isFirst = index == 0,
                            isEditing = isEditingItem, // 추가
                        )
                    }
                }
            }
        }
    }
}