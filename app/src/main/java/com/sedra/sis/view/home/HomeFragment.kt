package com.sedra.sis.view.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.FragmentHomeBinding
import com.sedra.sis.util.*
import com.sedra.sis.view.cart.CartActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    var binding: FragmentHomeBinding? = null

    @Inject
    lateinit var preferences: SharedPreferences
    var user: User? = null
    val viewModel: HomeViewModel by activityViewModels()

    var seconds = 0
    var running = false
    var wasRunning = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        user = getUserFromString(preferences.getString(PREF_PARENT_USER, "") ?: "")
        binding!!.userName.text = user?.name
//        viewModel.running.observe(viewLifecycleOwner) {
//            if (it) {
//                startCounter()
//            } else {
//                stopCounter()
//            }
//        }
        if (savedInstanceState != null) {
            seconds = savedInstanceState
                .getInt("seconds");
            running = savedInstanceState
                .getBoolean("running");
            wasRunning = savedInstanceState
                .getBoolean("wasRunning");
        }
        runTimer()
        binding?.apply {
            includeTimer.playTimer.setOnClickListener {
                onClickStart()
            }
            includeTimer.stopTimer.setOnClickListener {
                onClickStop()
            }
            includeTimer.pauseTimer.setOnClickListener {
                onCLickPause()
            }
            goToCart.setOnClickListener {
                startActivity(Intent(requireContext(), CartActivity::class.java))
            }
            workoutNum.text = preferences.getInt(PREF_WORKOUT_NUMBER, 0).toString()
            workoutMin.text = preferences.getInt(PREF_WORKOUT_MINS, 0).toString()
            caloriesBurned.text = preferences.getLong(PREF_BURNED_CAL, 0).toString()
            sendQuestion.setOnClickListener {
                askQuestion(questionText.text.toString())
            }
        }
        showBestScore(preferences.getInt(PREF_BEST_SCORE, 0))
    }

    private fun askQuestion(question: String) {
        viewModel.askQuestion(
            "Bearer ${user?.api_token}",
            user?.id ?: 0,
            question
        ).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show()
                        binding?.questionText?.setText("")
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, "Please, Enter password", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState
            .putInt("seconds", seconds);
        outState
            .putBoolean("running", running);
        outState
            .putBoolean("wasRunning", wasRunning);
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running;
        running = false;

    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true;
        }

    }

    private fun onClickStart() {
        running = true;
    }

    private fun onClickStop() {
        if (preferences.getLong(PREF_BEST_SCORE, 0) < seconds) {
            saveBestScore(seconds)
            showBestScore(seconds)
        }
        running = false
        seconds = 0
    }

    private fun saveBestScore(seconds: Int) {
        val editor = preferences.edit()
        editor.putInt(PREF_BEST_SCORE, seconds)
        editor.apply()
    }

    fun onCLickPause() {
        running = false
    }


    fun runTimer() {
        val handler = Handler();

        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600;
                val minutes = (seconds % 3600) / 60;
                val secs = seconds % 60;
                val time = String.format(
                    Locale.getDefault(),
                    "%d:%02d:%02d", hours,
                    minutes, secs
                )
                binding?.includeTimer?.timeView?.text = time
                if (running) {
                    seconds++
                }

                handler.postDelayed(this, 1000);
            }
        })

    }


    fun showBestScore(sec: Int) {
        val hours = sec / 3600
        val minutes = (sec % 3600) / 60
        val secs = sec % 60
        val time = String.format(
            Locale.getDefault(),
            "%d:%02d:%02d", hours,
            minutes, secs
        )
        binding?.includeTimer?.bestScore?.text = "BEST SCORE: $time"
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}