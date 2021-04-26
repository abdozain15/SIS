package com.sedra.sis.data.model

data class Product(
        val created_at: String,
        val department_id: Int,
        val description: String,
        val gallery: String,
        val id: Int,
        val image_path: String,
        val main_image: String,
        val name: String,
        val price: Int,
        val quantity: Int,
        val sale_price: Int,
        val updated_at: String
)