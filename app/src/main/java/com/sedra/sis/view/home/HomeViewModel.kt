package com.sedra.sis.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    val running  = MutableLiveData<Boolean>()
    var seconds = 0

    fun startRunning(){
        running.postValue(true)
    }

    fun stopRunning(){
        running.postValue(true)
        seconds = 0
    }
    fun pauseRunning(){
        running.postValue(true)
        seconds = 0
    }
}