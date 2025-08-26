package com.young.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.young.mytodo.ui.theme.MyTodoTheme
import com.young.mytodo.domain.util.TodoAndroidViewModelFactory
import com.young.mytodo.ui.main.HomeScreen
import com.young.mytodo.ui.main.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTodoTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = TodoAndroidViewModelFactory(application),
                )
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}