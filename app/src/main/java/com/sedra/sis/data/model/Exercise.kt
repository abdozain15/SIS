package com.sedra.sis.data.model

data class Exercise(
        val created_at: String,
        val exercies_img: String,
        val id: Int,
        val image_path: String,
        val minutes: Int,
        val name: String,
        val status: Int,
        val updated_at: String,
        val video: String,
        val video_path: String,
        val workout_id: Int
)