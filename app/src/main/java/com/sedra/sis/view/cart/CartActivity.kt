package com.sedra.sis.view.cart

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.sedra.sis.R
import com.sedra.sis.data.model.*
import com.sedra.sis.databinding.ActivityCartBinding
import com.sedra.sis.util.*
import com.sedra.sis.view.shopping.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {


    var binding: ActivityCartBinding? = null
    var productAdapter: ProductAdapter? = null

    @Inject
    lateinit var preferences: SharedPreferences
    var currentUser: User? = null
    val viewModel: CartViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        currentUser = getUserFromString(preferences.getString(PREF_PARENT_USER, "") ?: "")
        setUpUi()
    }

    private fun setUpUi() {
        binding?.apply {
            productAdapter = ProductAdapter(object : OnProductClicked {
                override fun onClick(view: View, product: Product) {
                    GoTo.productDetails(this@CartActivity, product)
                }
            })
            cartRv.layoutManager =
                GridLayoutManager(this@CartActivity, 2, GridLayoutManager.VERTICAL, false)
            cartRv.adapter = productAdapter
            confirmOrder.setOnClickListener {
                sendOrder()
            }
        }
        productAdapter?.submitList(Cart.hashMap.keys.toList())
    }

    private fun sendOrder() {
        var s = 0
        Cart.hashMap.forEach {
            s += (it.key.price * it.value)
        }
        val orderRequest: CartRequest = CartRequest(
            s,
            currentUser?.id ?: 0,
            Cart.hashMap.map { CartItem(it.key.id, it.value) }
        )
        viewModel.confirmOrder(
            "Bearer ${currentUser?.api_token}",
            orderRequest
        ).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { baseResponse ->
                            if (baseResponse.status) {
                                finish()
                            } else {
                                Toast.makeText(this, baseResponse.msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

}