package com.tlcabrera.apparquitectura.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tlcabrera.apparquitectura.data.model.Task
import com.tlcabrera.apparquitectura.databinding.ItemTaskBinding

class TaskAdapter(
    private val onToggle: (Long) -> Unit,
    private val onDelete: (Long) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvDescription.text = task.description

            // ✅ Feedback visual: tachado cuando está completada
            binding.tvTitle.paintFlags = if (task.isCompleted) {
                binding.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            binding.cbCompleted.isChecked = task.isCompleted

            // Evita disparar el listener al reciclar la vista
            binding.cbCompleted.setOnCheckedChangeListener(null)
            binding.cbCompleted.setOnCheckedChangeListener { _, _ -> onToggle(task.id) }
            binding.btnDelete.setOnClickListener { onDelete(task.id) }
        }
    }

    // ✅ DiffUtil: compara items eficientemente sin redibujar toda la lista
    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(old: Task, new: Task) = old.id == new.id
        override fun areContentsTheSame(old: Task, new: Task) = old == new
    }
}