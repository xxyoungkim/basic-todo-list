package com.young.mytodo.domain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.young.mytodo.data.repository.RoomTodoRepository
import com.young.mytodo.domain.repository.TodoRepository
import com.young.mytodo.ui.main.MainViewModel

class TodoAndroidViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application = application) {

    private val repository: TodoRepository by lazy {
        RoomTodoRepository(application)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, repository) as T
        }
        return super.create(modelClass)
    }
}