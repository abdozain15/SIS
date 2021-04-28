package com.sedra.sis.util

import android.view.View
import com.sedra.sis.data.model.Product

interface OnProductClicked {
    fun onClick(view: View, product: Product)
}