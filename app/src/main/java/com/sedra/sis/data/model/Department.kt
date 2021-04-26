package com.sedra.sis.data.model

data class Department(
        val created_at: String,
        val id: Int,
        val name: String,
        val products: List<Product>,
        val updated_at: String
)