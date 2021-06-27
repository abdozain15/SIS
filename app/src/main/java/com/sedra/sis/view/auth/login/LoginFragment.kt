package com.sedra.sis.view.auth.login

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.FragmentLoginBinding
import com.sedra.sis.util.GoTo
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.Status.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var preferences: SharedPreferences

    var binding: FragmentLoginBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setupUI()
    }

    private fun setupUI() {
        binding?.apply {
            newUserButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpPersonalDataFragment)
            }
            loginButton.setOnClickListener{
                checkLoginData(
                    loginEmail.text.toString(),
                    loginPassword.text.toString()
                )
            }
        }
    }

    private fun checkLoginData(email: String, password: String) {
        when {
            email.isEmpty() -> {
                Toast.makeText(context, "Please, Enter your email", Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(context, "Please, Enter your password", Toast.LENGTH_SHORT).show()
            }
            else -> {
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        resource.data?.let { baseResponse ->
                            if (baseResponse.status) {
                                saveUser(baseResponse.data)
                            } else {
                                Toast.makeText(context, baseResponse.msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    }
                    LOADING -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUser(data: User?) {
        val gson = Gson()
        val hashMapString = gson.toJson(data!!)
        val editor = preferences.edit()
        editor.putString(PREF_PARENT_USER, hashMapString)
        editor.apply()
        GoTo.mainActivity(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}