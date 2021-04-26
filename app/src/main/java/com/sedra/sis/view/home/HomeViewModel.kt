package com.sedra.sis.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sedra.sis.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        val apiService: ApiService
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
    fun pauseRunning(){
        running.postValue(true)
        seconds = 0
    }
}