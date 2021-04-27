package com.sedra.sis.view.home

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.FragmentHomeBinding
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.getUserFromString
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
        viewModel.running.observe(viewLifecycleOwner) {
            if (it) {
                startCounter()
            } else {
                stopCounter()
            }
        }
        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds = savedInstanceState
                .getInt("seconds");
            running = savedInstanceState
                .getBoolean("running");
            wasRunning = savedInstanceState
                .getBoolean("wasRunning");
        }
        runTimer();
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
        }
    }

    private fun stopCounter() {

    }

    private fun startCounter() {

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

    fun onClickStop() {
        running = false;
        seconds = 0;
    }

    fun onCLickPause() {
        running = false;
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
                );
                binding?.includeTimer?.timeView?.text = time
                if (running) {
                    seconds++
                }

                handler.postDelayed(this, 1000);
            }
        })

    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}