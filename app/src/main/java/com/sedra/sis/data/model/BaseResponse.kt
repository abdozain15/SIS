package com.sedra.sis.data.model

data class BaseResponse<T>(
    val status: Boolean,
    val errNum: String,
    val msg: String,
    val data: T?
)