package com.sedra.sis.view.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sedra.sis.R
import com.sedra.sis.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    var binding: FragmentLoginBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding?.apply {
            newUserButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpPersonalDataFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}