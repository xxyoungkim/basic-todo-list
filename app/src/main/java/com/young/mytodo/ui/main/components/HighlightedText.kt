package com.young.mytodo.ui.main.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun HighlightedText(
    fullText: String,
    keyword: String,
    highlightColor: Color = MaterialTheme.colorScheme.primary,
    normalColor: Color = MaterialTheme.colorScheme.onBackground,
    isDone: Boolean,
    modifier: Modifier = Modifier,
) {
    if (keyword.isEmpty()) {
        Text(
            text = fullText,
            color = if (isDone) Color.Gray else normalColor,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
            ),
            modifier = modifier
        )
        return
    }

    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        val lowerText = fullText.lowercase()
        val lowerKeyword = keyword.lowercase()

        while (currentIndex < fullText.length) {
            val startIndex = lowerText.indexOf(lowerKeyword, startIndex = currentIndex)
            if (startIndex == -1) {
                // 남은 부분을 그대로 추가
                append(fullText.substring(currentIndex))
                break
            }

            // 검색어 앞 부분 추가
            append(fullText.substring(currentIndex, startIndex))

            // 검색어 부분 강조
            pushStyle(
                SpanStyle(
                    color = highlightColor,
                    fontWeight = FontWeight.Bold
                )
            )
            append(fullText.substring(startIndex, startIndex + keyword.length))
            pop()

            // 다음 검색 시작 지점 이동
            currentIndex = startIndex + keyword.length
        }
    }

    Text(
        text = annotatedString,
        color = if (isDone) Color.Gray else normalColor,
        style = MaterialTheme.typography.bodyLarge.copy(
            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
        ),
        modifier = modifier
    )
}