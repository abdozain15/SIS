package com.sedra.sis.view.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.FragmentHomeBinding
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.getUserFromString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    var binding: FragmentHomeBinding? = null
    @Inject
    lateinit var preferences: SharedPreferences
    var user: User? = null
    val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        user = getUserFromString(preferences.getString(PREF_PARENT_USER,"")?:"")
        binding!!.userName.text = user?.name
        viewModel.running.observe(viewLifecycleOwner){
            if (it){
                startCounter()
            }else{
                stopCounter()
            }
        }
    }

    private fun stopCounter() {

    }

    private fun startCounter() {

    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}