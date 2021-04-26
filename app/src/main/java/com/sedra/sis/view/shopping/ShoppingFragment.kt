package com.sedra.sis.view.shopping

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedra.sis.R
import com.sedra.sis.data.model.Department
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.FragmentShopBinding
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.Status.*
import com.sedra.sis.util.getUserFromString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment : Fragment(R.layout.fragment_shop) {

    var binding: FragmentShopBinding? = null
    val viewModel: ShoppingViewModel by viewModels()

    @Inject
    lateinit var preferences: SharedPreferences
    var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShopBinding.bind(view)
        user = getUserFromString(preferences.getString(PREF_PARENT_USER, "") ?: "")
        getDepartments()
    }

    private fun getDepartments() {
        user?.let { user ->

            viewModel.getShoppingDepartments("Bearer ${user.api_token}").observe(viewLifecycleOwner) {
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
                            Log.e("TAG", "getDepartments:${resources.message}")
                        }
                        LOADING -> {

                        }
                    }
                }
            }
        }
    }

    private fun populateAdapters(departments: List<Department>?) {
        if (departments.isNullOrEmpty()) return
        val departmentAdapter = DepartmentAdapter()
        binding?.apply {
            departmentRv.layoutManager = LinearLayoutManager(requireContext())
            departmentRv.adapter = departmentAdapter
        }
        departmentAdapter.submitList(departments)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        user = null
    }


}