package com.tlcabrera.apparquitectura.ui

import androidx.lifecycle.ViewModel
import com.tlcabrera.apparquitectura.data.model.Task
import com.tlcabrera.apparquitectura.domain.usecase.AddTaskUseCase
import com.tlcabrera.apparquitectura.domain.usecase.DeleteTaskUseCase
import com.tlcabrera.apparquitectura.domain.usecase.GetTasksUseCase
import com.tlcabrera.apparquitectura.domain.usecase.ToggleTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado de la UI — todo lo que la pantalla necesita saber en un solo objeto
data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class TaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val toggleTaskUseCase: ToggleTaskUseCase
) : ViewModel() {

    // ✅ MutableStateFlow privado (solo el ViewModel puede escribir)
    private val _uiState = MutableStateFlow(TaskUiState())

    // ✅ StateFlow público de solo lectura (la UI solo puede leer)
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTasks()  // Carga inicial al crear el ViewModel
    }

    private fun loadTasks() {
        _uiState.update { it.copy(tasks = getTasksUseCase()) }
    }

    // ── Acciones que la UI puede invocar ──────────────────────────────────

    fun addTask(title: String, description: String) {
        addTaskUseCase(title, description)
            .onSuccess {
                // Limpia error y recarga lista
                _uiState.update { it.copy(errorMessage = null) }
                loadTasks()
            }
            .onFailure { error ->
                // Propaga el mensaje de error al estado de UI
                _uiState.update { it.copy(errorMessage = error.message) }
            }
    }

    fun deleteTask(id: Long) {
        deleteTaskUseCase(id)
        loadTasks()
    }

    fun toggleTask(id: Long) {
        toggleTaskUseCase(id)
        loadTasks()
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}