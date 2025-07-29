package com.young.mytodo.ui.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.young.mytodo.R
import com.young.mytodo.domain.model.Todo
import com.young.mytodo.ui.theme.MyTodoTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TodoItem(
    todo: Todo,
    onClick: (todo: Int) -> Unit = {},
    onDeleteClick: (id: Int) -> Unit = {},
) {
    val format = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(todo.uid) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 삭제 아이콘
            Icon(
                painter = painterResource(id = R.drawable.outline_delete_24),
                contentDescription = null,
                tint = Color(0xFFA51212),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onDeleteClick(todo.uid) }
            )

            // 내용
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    format.format(Date(todo.date)),
                    color = if (todo.isDone) Color.Gray else MaterialTheme.colorScheme.onBackground,
                    style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None),
                )
                Text(
                    todo.title,
                    color = if (todo.isDone) Color.Gray else MaterialTheme.colorScheme.onBackground,
                    style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None),
                )
            }

            if (todo.isDone) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_done_24),
                    contentDescription = null,
                    tint = Color(0xFF00BCD4),
                )
            }
        }
    }
}