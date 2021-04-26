package com.sedra.sis.view.auth.signup

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.FragmentSignUpLoginDataBinding
import com.sedra.sis.util.GoTo
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.Status.*
import com.sedra.sis.view.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpLoginDataFragment : Fragment(R.layout.fragment_sign_up_login_data) {

    val viewModel: AuthViewModel by activityViewModels()
    var binding: FragmentSignUpLoginDataBinding? = null

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpLoginDataBinding.bind(view)
        binding?.apply {
            confirmRegister.setOnClickListener {
                val password = registerPassword.text.toString()
                val confirmPassword = registerConfirmPassword.text.toString()
                val email = registerEmail.text.toString()
                when {
                    email.isEmpty() -> {
                        Toast.makeText(context, "Please, Enter email", Toast.LENGTH_SHORT).show()
                    }
                    password.isEmpty() -> {
                        Toast.makeText(context, "Please, Enter password", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "password don't match", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        register(email, password)
                    }
                }
            }
        }
    }

    private fun register(email: String, password: String) {
        viewModel.register(email, password).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        resource.data?.let { response ->
                            if (response.status) {
                                saveUser(response.data)
                            } else {
                                Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    ERROR -> {
                        Toast.makeText(context, "Please, Enter password", Toast.LENGTH_SHORT).show()
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