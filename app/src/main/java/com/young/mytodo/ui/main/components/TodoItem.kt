package com.young.mytodo.ui.main.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.young.mytodo.R
import com.young.mytodo.domain.model.Todo
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun TodoItem(
    todo: Todo,
    onClick: (todo: Int) -> Unit = {},
    onDeleteClick: (id: Int) -> Unit = {},
    onUpdateClick: (id: Int) -> Unit = {},
    isFirst: Boolean,
    isEditing: Boolean = false,
    searchQuery: String,
) {
    val actionWidth = 50.dp
    val actionWidthPx = with(LocalDensity.current) { actionWidth.toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (todo.isDone) offsetX else offsetX * 2,
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
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
            modifier = Modifier.padding(16.dp, 0.dp)
        )
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
                .background(
                    if (isEditing) MaterialTheme.colorScheme.surfaceContainer
                    else MaterialTheme.colorScheme.surface
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clickable {
                    println("클릭한 todoItem: id = ${todo.uid}, title = ${todo.title}")
                    onClick(todo.uid)
                },
        ) {
            val iconWidth = 36.dp

            // 내용
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .width(iconWidth)
                        .padding(8.dp, 0.dp)
                ) {
                    if (todo.isDone) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.outline_done_24
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .size(18.dp) // 아이콘 크기
                                .width(iconWidth)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    HighlightedText(
                        fullText = todo.title,
                        keyword = searchQuery,
                        isDone = todo.isDone
                    )
                }
            }
        }

        if (todo.isDone) {
            // 삭제 버튼 (배경)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset { IntOffset(x = (animatedOffsetX + actionWidthPx).roundToInt(), y = 0) }
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
        } else {
            // 수정 버튼 (배경)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset { IntOffset(x = (animatedOffsetX + actionWidthPx).roundToInt(), y = 0) }
                    .width(actionWidth)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        onUpdateClick(todo.uid)
                        offsetX = 0f
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_edit_24),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .width(actionWidth)
                )
            }
            // 삭제 버튼 (배경)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset {
                        IntOffset(
                            x = (animatedOffsetX + actionWidthPx + actionWidthPx).roundToInt(),
                            0
                        )
                    }
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
}