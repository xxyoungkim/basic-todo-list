package com.young.mytodo.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val searchQuery by viewModel.searchQuery.collectAsState()
    var isSearching by rememberSaveable { mutableStateOf(false) }

    val groupedTodos by viewModel.groupedItems.collectAsState()
    val isInitialized by viewModel.isInitialized.collectAsState()

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
                        CustomTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = "검색어를 입력해 주세요.",
                            keyboardActions = KeyboardActions(onDone = {
                                viewModel.updateSearchQuery(
                                    searchQuery
                                )
                                focusManager.clearFocus()
                            }),
                            singleLine = true,
                        )
                    } else {
                        Text("I CAN DO IT", color = MaterialTheme.colorScheme.primaryContainer)
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
                                tint = MaterialTheme.colorScheme.primaryContainer,
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            isSearching = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "검색",
                                tint = MaterialTheme.colorScheme.primaryContainer,
                            )
                        }
                    }
                },
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
                .clickable(
                    // 리플(ripple) 효과 제거
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                }
        ) {
            when {
                !isInitialized -> {
                    // 아직 초기화되지 않은 상태
                    LoadingScreen()
                }

                groupedTodos.isEmpty() -> {
                    // 초기화 완료 -> 데이터가 없는 상태
                    EmptyState(
                        message = "할 일이 존재하지 않습니다.",
                        modifier = Modifier.weight(1f)
                    )
                }

                else -> {
                    // 할 일 목록 영역
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                focusManager.clearFocus()
                            },
                    ) {
                        groupedTodos.forEach { (date, todos) ->
                            // 날짜 헤더
                            item(key = "header_$date") {
                                Box {
                                    Surface(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 0.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                focusManager.clearFocus()
                                            },
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shadowElevation = 1.dp,
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 28.dp,
                                                vertical = 6.dp
                                            ),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = date,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            )
                                        }
                                    }
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 0.dp
                                    ),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                )
                            }

                            items(
                                items = todos,
                                key = { todo -> "todo_${todo.uid}" }
                            ) { todoItem ->
                                println("LazyColumn item= id: ${todoItem.uid}, title: ${todoItem.title}")
                                val isEditingItem = (todoItem.uid == editingTodo?.uid)
                                val isFirstInGroup = todos.indexOf(todoItem) == 0

                                Column(
                                    modifier = Modifier.clickable { focusManager.clearFocus() }
                                ) {
                                    TodoItem(
                                        todo = todoItem,
                                        onClick = { todoId ->
                                            println("toggle 요청: $todoId") // 디버깅용
                                            viewModel.toggle(todoId)
                                        },
                                        onDeleteClick = { todoId ->
                                            viewModel.deleteTodo(todoId)

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
                                        onUpdateClick = { todoId ->
                                            isEditing = true
                                            editingTodoId = todoId
                                            viewModel.startEditing(todoId)
                                        },
                                        isFirst = isFirstInGroup,
                                        isEditing = isEditingItem,
                                        searchQuery = searchQuery,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 하단 입력 영역
            BottomInputSection(
                inputText = text,
                onInputChange = { text = it },
                onInputTodo = {
                    if (text.isNotBlank()) {
                        if (editingTodo != null) {
                            viewModel.updateTodo(text)
                            editingTodoId = null
                            viewModel.cancelEditing()
                        } else {
                            viewModel.addTodo(text)
                        }
                        text = ""
                        focusManager.clearFocus() // 키보드 숨기기
                    }
                }
            )
        }
    }
}

@Composable
private fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BottomInputSection(
    inputText: String,
    onInputChange: (String) -> Unit,
    onInputTodo: () -> Unit,
) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CustomTextField(
                value = inputText,
                onValueChange = onInputChange,
                placeholder = "+ 할 일을 추가해 주세요.",
                keyboardActions = KeyboardActions(onDone = { onInputTodo() }),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24),
                        contentDescription = "추가",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onInputTodo() },
                        tint = MaterialTheme.colorScheme.primaryContainer,
                    )
                },
                singleLine = false,
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    focusedBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
    unfocusedBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
    textStyle: TextStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 16.sp
    ),
    placeholderStyle: TextStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
        fontSize = 16.sp
    ),
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // 키보드 옵션-확인 버튼 체크 모양
    keyboardActions: KeyboardActions = KeyboardActions(),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minHeight: Dp = 46.dp,
    animationDuration: Int = 300,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused) focusedBackgroundColor else unfocusedBackgroundColor,
        animationSpec = tween(animationDuration),
        label = "backgroundColor"
    )


    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChanged?.invoke(focusState.isFocused)
            },
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(cursorColor),
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = minHeight)
                    .clip(shape)
                    .background(backgroundColor)
                    .padding(contentPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =
                        if (trailingIcon != null) {
                            Arrangement.SpaceBetween
                        } else {
                            Arrangement.Start
                        }
                ) {
                    // Leading Icon
                    leadingIcon?.let { icon ->
                        icon()
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Text Input Area
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = placeholderStyle
                            )
                        }
                        innerTextField()
                    }

                    // Trailing Icon
                    trailingIcon?.let { icon ->
                        Spacer(modifier = Modifier.width(8.dp))
                        icon()
                    }
                }
            }
        }
    )
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 5.dp
        )
    }
}