package com.sedra.sis.view.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sedra.sis.data.DataRepository
import com.sedra.sis.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
        private val repository: DataRepository
) : ViewModel() {

    fun getShoppingDepartments(auth: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getShoppingDepartments(auth)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.localizedMessage.toString()))
        }
    }

}