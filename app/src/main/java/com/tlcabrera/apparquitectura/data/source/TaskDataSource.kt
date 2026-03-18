package com.tlcabrera.apparquitectura.data.source

import com.tlcabrera.apparquitectura.data.model.Task


class TaskDataSource {

    private val tasks = mutableListOf<Task>()

    private var idCounter = 1L

    // ── CRUD ──────────────────────────────────────────────────────────────

    fun getAll(): List<Task> = tasks.toList()  // Copia defensiva

    fun add(title: String, description: String): Task {
        val newTask = Task(
            id = idCounter++,
            title = title,
            description = description
        )
        tasks.add(newTask)
        return newTask
    }

    fun delete(id: Long) {
        tasks.removeAll { it.id == id }
    }

    fun toggleCompleted(id: Long) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            tasks[index] = tasks[index].copy(isCompleted = !tasks[index].isCompleted)
        }
    }
}