package com.sedra.sis.data.model

data class Workout(
        val created_at: String,
        val exercies: List<Exercise>,
        val id: Int,
        val name: String,
        val updated_at: String
)