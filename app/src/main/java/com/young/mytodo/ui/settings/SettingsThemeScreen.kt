package com.young.mytodo.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.young.mytodo.R
import com.young.mytodo.ui.settings.util.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemeScreen(
    onBackClick: () -> Unit,
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "테마 설정",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "모드",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // 테마 선택 옵션들
            ThemeMode.entries.forEach { mode ->
                ThemeSelectionItem(
                    mode = mode,
                    isSelected = currentThemeMode == mode,
                    onSelect = { onThemeModeChanged(mode) }
                )
            }
        }
    }
}

@Composable
private fun ThemeSelectionItem(
    mode: ThemeMode,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아이콘
        val iconRes = when (mode) {
            ThemeMode.LIGHT -> R.drawable.outline_light_mode_24
            ThemeMode.DARK -> R.drawable.outline_dark_mode_24
            ThemeMode.SYSTEM -> R.drawable.outline_brightness_auto_24
        }

        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = when (mode) {
                    ThemeMode.LIGHT -> "라이트 모드"
                    ThemeMode.DARK -> "다크 모드"
                    ThemeMode.SYSTEM -> "시스템 설정"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }

        // 선택 표시
        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.outline_check_24),
                contentDescription = "선택됨",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
    )
}