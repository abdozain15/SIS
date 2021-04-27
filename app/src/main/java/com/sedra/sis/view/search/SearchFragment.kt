package com.sedra.sis.view.search

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.FragmentSearchBinding
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.Status.*
import com.sedra.sis.util.getUserFromString
import com.sedra.sis.view.shopping.ProductAdapter
import com.sedra.sis.view.shopping.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    var binding: FragmentSearchBinding? = null
    var productAdapter: ProductAdapter? = null
    val viewModel: ShoppingViewModel by viewModels()

    @Inject
    lateinit var preferences: SharedPreferences
    var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        user = getUserFromString(preferences.getString(PREF_PARENT_USER, "") ?: "")
        setUpUi()
    }

    private fun setUpUi() {
        binding?.apply {
            productAdapter = ProductAdapter()
            searchButton.setOnClickListener {
                val searchQuery = searchEt.text.toString()
                if (searchQuery.isEmpty()) return@setOnClickListener
                search(searchQuery)
            }
            searchRv.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            searchRv.adapter = productAdapter
        }
    }

    private fun search(searchQuery: String) {
        viewModel.search("Bearer ${user?.api_token}", searchQuery).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        resource.data?.let { baseResponse ->
                            if (baseResponse.status) {
                                productAdapter?.submitList(baseResponse.data)
                            } else {
                                Toast.makeText(context, baseResponse.msg, Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


}