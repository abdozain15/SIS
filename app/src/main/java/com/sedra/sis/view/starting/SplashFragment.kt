package com.sedra.sis.view.starting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sedra.sis.R
import com.sedra.sis.util.GoTo
import com.sedra.sis.util.PREF_PARENT_USER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(2000)
            if (preferences.getString(PREF_PARENT_USER, "").isNullOrEmpty()) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                GoTo.mainActivity(requireContext())
            }
        }


    }

}