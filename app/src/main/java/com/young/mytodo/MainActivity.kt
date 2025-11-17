package com.young.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.young.mytodo.ui.main.MainViewModel
import com.young.mytodo.ui.navigation.TodoNavigation
import com.young.mytodo.ui.settings.util.ThemeMode
import com.young.mytodo.ui.settings.util.ThemePreferences
import com.young.mytodo.ui.theme.MyTodoTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * @AndroidEntryPoint - Hilt가 이 Activity에 의존성을 주입할 수 있도록 함
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var themePreferences: ThemePreferences
    private val themeMode = mutableStateOf(ThemeMode.SYSTEM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themePreferences = ThemePreferences(this)
        themeMode.value = themePreferences.getThemeMode()

        enableEdgeToEdge()
        setContent {
            MyTodoTheme(
                themeMode = themeMode.value
            ) {
                val viewModel: MainViewModel = hiltViewModel()
//                HomeScreen(viewModel = viewModel)
                TodoNavigation(
                    viewModel = viewModel,
                    currentThemeMode = themeMode.value,
                    onThemeModeChanged = { newMode ->
                        themeMode.value = newMode
                        themePreferences.setThemeMode(newMode)
                    }
                )
            }
        }
    }
}