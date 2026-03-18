package com.tlcabrera.apparquitectura.data.model
// ✅ PATRÓN: Data class de Kotlin
// • Genera equals(), hashCode(), copy(), toString() automáticamente
// • Inmutabilidad por defecto (val) → Single Source of Truth
// • El 'id' único permite identificar tareas en la lista sin depender de posición

data class Task(
        val id: Long,
        val title: String,
        val description: String = "",
        val isCompleted: Boolean = false
)