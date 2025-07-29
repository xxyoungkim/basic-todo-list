package com.young.mytodo.ui.main.components

import android.os.Build
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
import androidx.compose.ui.unit.dp
import com.young.mytodo.R
import com.young.mytodo.domain.model.Todo
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun TodoItem(
    todo: Todo,
    onClick: (todo: Int) -> Unit = {},
    onDeleteClick: (id: Int) -> Unit = {},
) {
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
                    dateFormat(todo),
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

@Composable
fun dateFormat(todo: Todo): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // API 26 이상만 실행
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val instant = Instant.ofEpochMilli(todo.date)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(localDateTime)
    } else {
        // API 26 미만
        val formatter = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        return formatter.format(Date(todo.date))
    }
}