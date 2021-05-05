package com.sedra.sis.view.workout

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.sedra.sis.R
import com.sedra.sis.data.model.Exercise
import com.sedra.sis.databinding.ListItemWorkoutBinding
import javax.inject.Inject

class ExerciseAdapter() : ListAdapter<Exercise, CustomViewHolder>(Companion) {

    @Inject
    lateinit var preferences: SharedPreferences
    var listener: ExerciseListener? = null

    companion object : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }
    }

//    private fun getSeenWorkouts() {
//        val savedString: String = preferences.getString(PREF_SEEN_EXERCISE, "")?:""
//        if (savedString.isNotEmpty()){
//            val arr = savedString.split(",")
//            for (i in arr){
//                seenWorkouts.add(i.toInt())
//            }
//        }
//    }


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
        itemBinding.root.setOnClickListener {
            if (listener != null) {
                listener?.onClick(it, currentExercise)
            }
        }
//        if (seenWorkouts.contains(currentExercise.id)){
//            itemBinding.imageView19.setImageResource(R.drawable.replay)
//        }else{
//            itemBinding.imageView19.setImageResource(R.drawable.play)
//        }
        itemBinding.textView33.text = currentExercise.name
        itemBinding.executePendingBindings()
    }
}