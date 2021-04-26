package com.sedra.sis.view.workout

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.data.model.Workout
import com.sedra.sis.databinding.FragmentWorkoutBinding
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.Status.*
import com.sedra.sis.util.getUserFromString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutFragment : Fragment(R.layout.fragment_workout) {

    var binding: FragmentWorkoutBinding? = null
    val viewModel: WorkoutViewModel by viewModels()

    @Inject
    lateinit var preferences: SharedPreferences
    var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutBinding.bind(view)
        user = getUserFromString(preferences.getString(PREF_PARENT_USER, "") ?: "")
        getWorkouts()
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
        val workoutAdapter = WorkoutAdapter()
        binding?.apply {
            workoutsRv.layoutManager = LinearLayoutManager(requireContext())
            workoutsRv.adapter = workoutAdapter
        }
        workoutAdapter.submitList(workouts)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        user = null
    }


}