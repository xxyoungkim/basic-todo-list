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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToThemeSettings: () -> Unit,
    onNavigateToExportSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "설정",
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
            // 테마 설정
            SettingsClickableItem(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_invert_colors_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                },
                title = "테마 설정",
                description = "어두운 테마 변경",
                onClick = onNavigateToThemeSettings,
            )

            // 데이터 내보내기
            SettingsClickableItem(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_download_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                },
                title = "데이터 내보내기",
                description = "데이터 파일 내보내기",
                onClick = onNavigateToExportSettings,
            )

            // 앱 정보
//            SettingsClickableItem(
//                icon = {
//                    Icon(
//                        imageVector = Icons.Default.Info,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onSurface,
//                        modifier = Modifier.size(24.dp)
//                    )
//                },
//                title = "앱 정보",
//                description = "버전 및 개발자 정보",
//                onClick = {
//                    // 앱 정보 화면으로 이동 또는 다이얼로그 표시
//                }
//            )

        }
    }
}

@Composable
private fun SettingsClickableItem(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
//            Text(
//                text = description,
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//            )
        }
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
    )
}