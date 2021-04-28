package com.sedra.sis.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sedra.sis.data.DataRepository
import com.sedra.sis.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: DataRepository
) : ViewModel() {

    val running = MutableLiveData<Boolean>()
    var seconds = 0

    fun startRunning() {
        running.postValue(true)
    }

    fun stopRunning() {
        running.postValue(true)
        seconds = 0
    }

    fun pauseRunning() {
        running.postValue(true)
        seconds = 0
    }


    fun askQuestion(auth: String, id: Int, body: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.askQuestions(auth, id, body)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = "Mac Or Code are wrong"))
        }
    }

}