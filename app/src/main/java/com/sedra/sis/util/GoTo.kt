package com.sedra.sis.util

import android.content.Context
import android.content.Intent
import com.sedra.sis.data.model.Product
import com.sedra.sis.view.home.MainActivity
import com.sedra.sis.view.productdetails.ProductDetailsActivity

object GoTo {

    fun mainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun productDetails(context: Context, product: Product) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(EXTRA_PRODUCT, product)
        context.startActivity(intent)
    }
}