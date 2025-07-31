package com.young.mytodo.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("오늘의 할 일") }
            )
        },
    ) { innerPadding ->
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
                placeholder = { Text("할 일") },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { viewModel.addTodo(text) }
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // 키보드 옵션-확인 버튼 체크 모양
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.addTodo(text)
                    text = ""
                    focusManager.clearFocus() // 키보드 숨기기
                }),
            )

            Divider()

            LazyColumn(
//                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            ) {
                itemsIndexed(viewModel.items.value) { index, todoItem ->
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
                                        message = "할 일 삭제됨",
                                        actionLabel = "취소",
                                        duration = SnackbarDuration.Short,
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.restoreTodo()
                                    }
                                }
                            },
                            isFirst = index == 0
                        )
                    }
                }
            }
        }
    }
}