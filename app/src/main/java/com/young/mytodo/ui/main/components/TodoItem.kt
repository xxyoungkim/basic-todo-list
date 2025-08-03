package com.young.mytodo.ui.main.components

import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.young.mytodo.R
import com.young.mytodo.domain.model.Todo
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun TodoItem(
    todo: Todo,
    onClick: (todo: Int) -> Unit = {},
    onDeleteClick: (id: Int) -> Unit = {},
    isFirst: Boolean,
) {
    val actionWidth = 60.dp
    val actionWidthPx = with(LocalDensity.current) { actionWidth.toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        label = "Offset Animation",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
    )

    // 스와이프 완료 후 자동으로 닫힘
    LaunchedEffect(offsetX) {
        if (offsetX <= -actionWidthPx) {
            delay(2000) // 2초 기다리기
            offsetX = 0f // 다시 닫기
        }
    }

    if (!isFirst) {
        HorizontalDivider(color = Color.LightGray)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // 스와이프가 절반 이상이면 열기
                        val threshold = actionWidthPx * 0.5f
                        offsetX = if (offsetX < -threshold) -actionWidthPx else 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        val newOffset = offsetX + dragAmount
                        // 오른쪽으로는 밀지 못하게 제한
                        offsetX = newOffset.coerceIn(-actionWidthPx, 0f)
                    },
                )
            }
    ) {
        // 콘텐츠 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .clickable { onClick(todo.uid) },
        ) {
            val iconWidth = 32.dp

            // 내용
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .width(iconWidth)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (todo.isDone)
                                R.drawable.twotone_check_box_24
                            else
                                R.drawable.outline_check_box_outline_blank_24
                        ),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(24.dp) // 아이콘 크기
                            .width(iconWidth)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        dateFormat(todo),
                        color = if (todo.isDone) Color.Gray else MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        todo.title,
                        color = if (todo.isDone) Color.Gray else MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
                        ),
                    )
                }
            }
        }

        // 삭제 버튼 (배경)
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset { IntOffset(x = (animatedOffsetX + actionWidthPx).roundToInt(), 0) }
                .width(actionWidth)
                .fillMaxHeight()
                .background(Color(0xFFA51212))
                .clickable {
                    onDeleteClick(todo.uid)
                    offsetX = 0f
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_delete_24),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .width(actionWidth)
            )
        }
    }
}

@Composable
fun dateFormat(todo: Todo): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // API 26 이상
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