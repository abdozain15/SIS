package com.sedra.sis.view.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sedra.sis.data.DataRepository
import com.sedra.sis.data.model.CartRequest
import com.sedra.sis.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    fun confirmOrder(auth: String, order: CartRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.sendOrder(auth, order)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.localizedMessage.toString()))
        }
    }

}