package com.young.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.young.mytodo.domain.util.TodoAndroidViewModelFactory
import com.young.mytodo.ui.main.HomeScreen
import com.young.mytodo.ui.main.MainViewModel
import com.young.mytodo.ui.theme.MyTodoTheme

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