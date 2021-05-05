package com.sedra.sis.view.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sedra.sis.data.model.Exercise
import com.sedra.sis.data.model.Workout
import com.sedra.sis.databinding.ListItemWorkoutDepartmentBinding


class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
class WorkoutAdapter(
    val listener: ExerciseListener
) : ListAdapter<Workout, CustomViewHolder>(Companion) {
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkoutDepartmentBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentWorkout = getItem(position)
        val itemBinding = holder.binding as ListItemWorkoutDepartmentBinding
        itemBinding.workout = currentWorkout
        itemBinding.nestedRecyclerView.setRecycledViewPool(viewPool)
        itemBinding.executePendingBindings()
        val nestedAdapter = itemBinding.nestedRecyclerView.adapter as ExerciseAdapter
        nestedAdapter.listener = listener
    }
}

interface ExerciseListener {
    fun onClick(view: View, exercise: Exercise)
}