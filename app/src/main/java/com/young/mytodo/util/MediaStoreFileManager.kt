package com.young.mytodo.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.young.mytodo.domain.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MediaStoreFileManager(private val context: Context) {

    /**
     * todo 목록을 Downloads 폴더에 텍스트 파일로 저장
     */
    suspend fun saveToDownloads(groupedTodos: Map<String, List<Todo>>): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val fileName = "todos_${getCurrentDateString()}.txt"
                val fileContent = generateFileContent(groupedTodos)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // API 29+ (Android 10+): MediaStore API 사용
                    saveWithMediaStore(fileName, fileContent)
                } else {
                    // API 28 이하: 직접 파일 시스템 접근
                    saveWithLegacyMethod(fileName, fileContent)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * API 29+ MediaStore를 사용한 파일 저장
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveWithMediaStore(fileName: String, content: String): Result<String> {
        return try {
            val contentResolver: ContentResolver = context.contentResolver

            // MediaStore에 파일 정보 등록
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain; charset=utf-8")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1) // 파일 작성 중임을 표시
            }

            // Downloads 컬렉션에 파일 생성
            val uri: Uri? = contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let { fileUri ->
                // 파일에 내용 쓰기
                contentResolver.openOutputStream(fileUri)?.use { outputStream ->
                    outputStream.write(content.toByteArray(Charsets.UTF_8))
                }
                // 파일 작성 완료 표시
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(fileUri, contentValues, null, null)

                Result.success("Downloads/$fileName")
            } ?: Result.failure(Exception("파일 생성에 실패했습니다."))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * API 28 이하를 위한 레거시 방법
     */
    private fun saveWithLegacyMethod(fileName: String, content: String): Result<String> {
        return try {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            // Downloads 폴더가 없으면 생성
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }

            val file = File(downloadsDir, fileName)

            FileOutputStream(file).use { outputStream ->
                outputStream.write(content.toByteArray(Charsets.UTF_8))
            }

            Result.success("Downloads/$fileName")

        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: SecurityException) {
            // 권한이 없는 경우
            Result.failure(Exception("저장 권한이 필요합니다."))
        }
    }

    /**
     * 파일 내용 생성
     */
    private fun generateFileContent(groupedTodos: Map<String, List<Todo>>): String {
        val builder = StringBuilder()

        builder.appendLine("생성일: ${getCurrentDateTimeString()}")
        builder.appendLine("=${"=".repeat(50)}")
        builder.appendLine()

        // 내용
        groupedTodos.forEach { (date, todos) ->
            // 날짜 헤더
            builder.appendLine(date)
            todos.sortedByDescending { it.date }.forEach { todo ->
                if (todo.isDone) {
                    builder.appendLine(" ✅ ${todo.title}")
                } else {
                    builder.appendLine(" ⬜ ${todo.title}")
                }

            }
            builder.appendLine()
        }

        builder.appendLine()
        builder.appendLine("=${"=".repeat(50)}")
        builder.appendLine("I CAN DO IT 앱에서 내보냄")

        return builder.toString()
    }

    private fun getCurrentDateString(): String {
        val formatter = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun getCurrentDateTimeString(): String {
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }

    /**
     * 권한 확인
     */
    fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API 29+에서는 MediaStore 사용시 특별한 권한이 필요하지 않음
            true
        } else {
            // API 28 이하에서는 WRITE_EXTERNAL_STORAGE 권한 필요
            context.packageManager.getPackageInfo(
                context.packageName,
                android.content.pm.PackageManager.GET_PERMISSIONS
            ).requestedPermissions?.contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == true
        }
    }
}