package com.sedra.sis.view.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sedra.sis.data.DataRepository
import com.sedra.sis.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
        private val repository: DataRepository
) : ViewModel() {

    fun getWorkouts(auth: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getWorkouts(auth)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.localizedMessage.toString()))
        }
    }

}