package com.tlcabrera.apparquitectura.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.tlcabrera.apparquitectura.R
import com.tlcabrera.apparquitectura.databinding.ActivityMainBinding
import com.tlcabrera.apparquitectura.ui.adapter.TaskAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // ✅ Delegate viewModels() usa la Factory para construir el ViewModel
    private val viewModel: TaskViewModel by viewModels { TaskViewModelFactory() }

    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAddButton()
        observeUiState()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            onToggle = { id -> viewModel.toggleTask(id) },
            onDelete = { id -> viewModel.deleteTask(id) }
        )
        binding.rvTasks.adapter = adapter
    }

    private fun setupAddButton() {
        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            viewModel.addTask(title, description)
            binding.etTitle.text?.clear()
            binding.etDescription.text?.clear()
        }
    }

    private fun observeUiState() {
        // ✅ repeatOnLifecycle: automáticamente pausa/reanuda la colección
        // según el ciclo de vida del Activity (evita leaks y actualizaciones en background)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Actualiza lista solo con los items que cambiaron (DiffUtil)
                    adapter.submitList(state.tasks)

                    // Muestra estado vacío
                    binding.tvEmpty.visibility =
                        if (state.tasks.isEmpty()) View.VISIBLE else View.GONE

                    // Muestra errores como Snackbar (no como Toast: mejora UX)
                    state.errorMessage?.let { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                        viewModel.clearError()
                    }
                }
            }
        }
    }
}