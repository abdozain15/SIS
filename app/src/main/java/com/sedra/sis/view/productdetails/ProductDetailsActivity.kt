package com.sedra.sis.view.productdetails

import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sedra.sis.R
import com.sedra.sis.data.model.Cart
import com.sedra.sis.data.model.Product
import com.sedra.sis.databinding.ActivityProductDetailsBinding
import com.sedra.sis.util.EXTRA_PRODUCT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {

    var binding: ActivityProductDetailsBinding? = null

    var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        setupUi()
    }


    private fun setupUi() {
        val currentProduct = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)
        binding?.apply {
            this.product = currentProduct
            back.setOnClickListener { finish() }
            Glide.with(root)
                .load(currentProduct?.image_path)
                .into(productImageDetails)
            textView40.paintFlags = textView40.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            increaseQuantity.setOnClickListener {
                quantity++
                increaseQuantity.text = "QUN\n$quantity"
                decreaseQuantity.text = "QUN\n$quantity"
            }
            decreaseQuantity.setOnClickListener {
                if (quantity > 0)quantity--
                increaseQuantity.text = "QUN\n$quantity"
                decreaseQuantity.text = "QUN\n$quantity"
            }
            addToCart.setOnClickListener {
                if (Cart.hashMap.contains(currentProduct)) {
                    Cart.hashMap[currentProduct!!] = Cart.hashMap[currentProduct]!! + quantity
                } else {
                    Cart.hashMap[currentProduct!!] = quantity
                }
                quantity = 1
                increaseQuantity.text = "QUN\n$quantity"
                decreaseQuantity.text = "QUN\n$quantity"
            }
        }
    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}