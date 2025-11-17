package com.young.mytodo.util

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun PermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    content: @Composable (requestPermission: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    var showRationale by remember { mutableStateOf(false) }

    // API 29 이상에서는 권한이 필요하지 않음
    val needsPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            showRationale = true
        }
    }

    // 권한 요청 함수
    val requestPermission = {
        if (needsPermission) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            // API 29+에서는 바로 실행
            onPermissionGranted()
        }
    }

    content(requestPermission)

    // 권한 거부 시 설명 다이얼로그
    if (showRationale) {
        AlertDialog(
            onDismissRequest = {
                showRationale = false
                onPermissionDenied()
            },
            title = { Text("저장 권한 필요") },
            text = {
                Text("파일을 Downloads 폴더에 저장하려면 저장소 접근 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationale = false
                        onPermissionDenied()
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }
}