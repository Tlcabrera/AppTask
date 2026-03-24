package com.tlcabrera.apparquitectura.domain.usecase

import com.tlcabrera.apparquitectura.data.model.Task
import com.tlcabrera.apparquitectura.data.repository.TaskRepository


// Obtener todas las tareas
class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): List<Task> = repository.getTasks()
}

// Agregar tarea — aquí vive la validación de negocio
class AddTaskUseCase(private val repository: TaskRepository) {
    operator fun invoke(title: String, description: String): Result<Task> {
        // ✅ Validación de negocio en el Use Case, no en la UI
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
        }
        if (title.length > 100) {
            return Result.failure(IllegalArgumentException("El título es demasiado largo"))
        }
        return Result.success(repository.addTask(title.trim(), description.trim()))
    }
}

// Eliminar tarea
class DeleteTaskUseCase(private val repository: TaskRepository) {
    operator fun invoke(id: Long) = repository.deleteTask(id)
}

// Marcar como completada/pendiente
class ToggleTaskUseCase(private val repository: TaskRepository) {
    operator fun invoke(id: Long) = repository.toggleTask(id)
}