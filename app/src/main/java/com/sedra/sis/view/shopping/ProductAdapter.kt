package com.sedra.sis.view.shopping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.sedra.sis.R
import com.sedra.sis.data.model.Product
import com.sedra.sis.databinding.ListItemShopBinding

class ProductAdapter : ListAdapter<Product, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemShopBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentProduct = getItem(position)
        val itemBinding = holder.binding as ListItemShopBinding
        Glide.with(holder.itemView)
                .load(currentProduct.image_path)
                .placeholder(R.drawable.logo)
                .into(itemBinding.productImage)
        itemBinding.apply {
            textView30.text = currentProduct.name
            salePrice.text = currentProduct.sale_price.toString()
            price.text = currentProduct.price.toString()
        }
        itemBinding.executePendingBindings()
    }
}