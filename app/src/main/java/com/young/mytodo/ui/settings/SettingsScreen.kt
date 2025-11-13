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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.young.mytodo.R
import com.young.mytodo.ui.main.ExportState
import com.young.mytodo.util.PermissionHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToThemeSettings: () -> Unit,
    onExportData: () -> Unit,
    exportState: ExportState,
    onClearExportState: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 내보내기 상태에 따른 스낵바 표시
    LaunchedEffect(exportState) {
        when (exportState) {
            is ExportState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "${exportState.filePath}에 저장되었습니다.",
                        duration = SnackbarDuration.Long
                    )
                    onClearExportState()
                }
            }

            is ExportState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = exportState.message,
                        duration = SnackbarDuration.Long
                    )
                    onClearExportState()
                }
            }

            else -> { /* 다른 상태는 처리하지 않음 */
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                onClick = onNavigateToThemeSettings
            )

            // 데이터 내보내기 (권한 처리 포함)
            PermissionHandler(
                onPermissionGranted = { onExportData() },
                onPermissionDenied = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "저장 권한이 없어 파일을 내보낼 수 없습니다.",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            ) { requestPermission ->
                SettingsClickableItem(
                    icon = {
                        if (exportState is ExportState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.outline_download_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    title = "데이터 내보내기",
                    description = when (exportState) {
                        is ExportState.Loading -> "내보내는 중..."
                        else -> "할 일 목록을 Downloads 폴더에 저장"
                    },
                    onClick = {
                        if (exportState !is ExportState.Loading) {
                            requestPermission()
                        }
                    },
                    enabled = exportState !is ExportState.Loading
                )
            }

            // 데이터 내보내기
//            SettingsClickableItem(
//                icon = {
//                    Icon(
//                        painter = painterResource(R.drawable.outline_download_24),
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onSurface,
//                        modifier = Modifier.size(24.dp)
//                    )
//                },
//                title = "데이터 내보내기",
//                description = "데이터 텍스트 파일 내보내기",
//                onClick = { }
//            )
            // 앱 정보
            SettingsClickableItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                },
                title = "앱 정보",
                description = "버전 및 개발자 정보",
                onClick = {
                    // 앱 정보 화면으로 이동 또는 다이얼로그 표시
                }
            )

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