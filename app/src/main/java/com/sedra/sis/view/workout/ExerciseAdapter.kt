package com.sedra.sis.view.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.sedra.sis.R
import com.sedra.sis.data.model.Exercise
import com.sedra.sis.databinding.ListItemWorkoutBinding

class ExerciseAdapter : ListAdapter<Exercise, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkoutBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentExercise = getItem(position)
        val itemBinding = holder.binding as ListItemWorkoutBinding
        Glide.with(holder.itemView)
                .load(currentExercise.image_path)
                .placeholder(R.drawable.logo)
                .into(itemBinding.workoutImage)
        itemBinding.textView33.text = currentExercise.name
        itemBinding.executePendingBindings()
    }
}