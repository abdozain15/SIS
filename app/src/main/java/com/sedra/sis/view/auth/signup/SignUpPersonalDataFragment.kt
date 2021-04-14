package com.sedra.sis.view.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sedra.sis.R
import com.sedra.sis.databinding.FragmentSignUpPersonalDataBinding

class SignUpPersonalDataFragment : Fragment(R.layout.fragment_sign_up_personal_data) {
    var binding: FragmentSignUpPersonalDataBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpPersonalDataBinding.bind(view)
        binding?.apply {
            nextSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_signUpPersonalDataFragment_to_signUpLoginDataFragment)
            }
        }
    }
}