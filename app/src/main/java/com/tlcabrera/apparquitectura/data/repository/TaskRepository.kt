package com.tlcabrera.apparquitectura.data.repository

import com.tlcabrera.apparquitectura.data.model.Task
import com.tlcabrera.apparquitectura.data.source.TaskDataSource

class TaskRepository(
    private val dataSource: TaskDataSource  // Inyección de dependencia manual
) {
    fun getTasks(): List<Task> = dataSource.getAll()

    fun addTask(title: String, description: String): Task =
        dataSource.add(title, description)

    fun deleteTask(id: Long) = dataSource.delete(id)

    fun toggleTask(id: Long) = dataSource.toggleCompleted(id)
}