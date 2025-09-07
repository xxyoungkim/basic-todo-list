package com.young.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.young.mytodo.ui.theme.MyTodoTheme
import com.young.mytodo.domain.util.TodoAndroidViewModelFactory
import com.young.mytodo.ui.main.HomeScreen
import com.young.mytodo.ui.main.MainViewModel
import com.young.mytodo.ui.navigation.TodoNavigation
import com.young.mytodo.ui.settings.util.ThemeMode
import com.young.mytodo.ui.settings.util.ThemePreferences

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
                val viewModel: MainViewModel = viewModel(
                    factory = TodoAndroidViewModelFactory(application),
                )
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