package com.sedra.sis.view.workout

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedra.sis.R
import com.sedra.sis.data.model.Exercise
import com.sedra.sis.data.model.User
import com.sedra.sis.data.model.Workout
import com.sedra.sis.databinding.FragmentWorkoutBinding
import com.sedra.sis.util.*
import com.sedra.sis.util.Status.*
import com.sedra.sis.view.PlayExerciseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToLong


@AndroidEntryPoint
class WorkoutFragment : Fragment(R.layout.fragment_workout) {

    var binding: FragmentWorkoutBinding? = null
    val viewModel: WorkoutViewModel by viewModels()
    val seenExercise = ArrayList<Int>()

    @Inject
    lateinit var preferences: SharedPreferences
    var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutBinding.bind(view)
        user = getUserFromString(preferences.getString(PREF_PARENT_USER, "") ?: "")
        getSeenWorkouts()
        getWorkouts()
    }

    private fun getSeenWorkouts() {
        val savedString: String = preferences.getString(PREF_SEEN_EXERCISE, "") ?: ""
        if (savedString.isNotEmpty()) {
            val arr = savedString.split(",")
            for (i in arr) {
                seenExercise.add(i.toInt())
            }
        }
    }

    private fun getWorkouts() {
        user?.let { user ->

            viewModel.getWorkouts("Bearer ${user.api_token}").observe(viewLifecycleOwner) {
                it?.let { resources ->
                    when (resources.status) {
                        SUCCESS -> {
                            resources.data?.let { baseResponse ->
                                if (baseResponse.status) {
                                    populateAdapters(baseResponse.data)
                                } else {

                                }
                            }
                        }
                        ERROR -> {
                            Log.e("TAG", "getWorkouts:${resources.message}")
                        }
                        LOADING -> {

                        }
                    }
                }
            }
        }
    }

    private fun populateAdapters(workouts: List<Workout>?) {
        if (workouts.isNullOrEmpty()) return
        val workoutAdapter = WorkoutAdapter(object : ExerciseListener {
            override fun onClick(view: View, exercise: Exercise) {
                addExerciseToSeen(exercise)
            }
        })
        binding?.apply {
            workoutsRv.layoutManager = LinearLayoutManager(requireContext())
            workoutsRv.adapter = workoutAdapter
        }
        workoutAdapter.submitList(workouts)
    }

    private fun addExerciseToSeen(exercise: Exercise) {
        val editor = preferences.edit()
        editor.putBoolean(exercise.id.toString(), true)
        val workoutNumber = preferences.getInt(PREF_WORKOUT_NUMBER, 0)
        editor.putInt(PREF_WORKOUT_NUMBER, workoutNumber + 1)
        val workoutMin = preferences.getInt(PREF_WORKOUT_MINS, 0)
        editor.putInt(PREF_WORKOUT_MINS, workoutMin + exercise.minutes)
        val burnedCalories = getCaloriesBurned(exercise)
        val previousBurnedCalories = preferences.getLong(PREF_BURNED_CAL, 0)
        editor.putLong(PREF_BURNED_CAL, previousBurnedCalories + burnedCalories)
        editor.apply()
        openExercise(exercise)
    }

    private fun openExercise(exercise: Exercise) {
        val intent = Intent(requireContext(), PlayExerciseActivity::class.java)
        intent.putExtra(EXTRA_EXERCISE, exercise.video_path)
        startActivity(intent)
    }

    private fun getCaloriesBurned(exercise: Exercise): Long {
        return (exercise.minutes * user!!.weight * 3.5 / 200).roundToLong()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        user = null
    }


}