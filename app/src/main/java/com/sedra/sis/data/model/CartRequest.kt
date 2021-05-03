package com.sedra.sis.data.model

data class CartRequest(
    val total_price: Int,
    val user_id: Int,
    var item: List<CartItem> = ArrayList()
)