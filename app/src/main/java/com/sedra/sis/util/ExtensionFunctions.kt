package com.sedra.sis.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sedra.sis.data.model.Exercise
import com.sedra.sis.view.workout.ExerciseAdapter

@BindingAdapter(value = ["setExercises"])
fun RecyclerView.setExercises(exercise: List<Exercise>?) {
    if (exercise != null) {
        val exerciseAdapter = ExerciseAdapter()
        exerciseAdapter.submitList(exercise)
        adapter = exerciseAdapter
    }
}