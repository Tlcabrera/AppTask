package com.tlcabrera.apparquitectura.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tlcabrera.apparquitectura.data.repository.TaskRepository
import com.tlcabrera.apparquitectura.data.source.TaskDataSource
import com.tlcabrera.apparquitectura.domain.usecase.AddTaskUseCase
import com.tlcabrera.apparquitectura.domain.usecase.DeleteTaskUseCase
import com.tlcabrera.apparquitectura.domain.usecase.GetTasksUseCase
import com.tlcabrera.apparquitectura.domain.usecase.ToggleTaskUseCase

class TaskViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            // ✅ Composición manual de dependencias (DI manual sin Hilt/Koin)
            val dataSource = TaskDataSource()
            val repository = TaskRepository(dataSource)

            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(
                getTasksUseCase    = GetTasksUseCase(repository),
                addTaskUseCase     = AddTaskUseCase(repository),
                deleteTaskUseCase  = DeleteTaskUseCase(repository),
                toggleTaskUseCase  = ToggleTaskUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}