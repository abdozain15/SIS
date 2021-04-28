package com.sedra.sis.view.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sedra.sis.data.DataRepository
import com.sedra.sis.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {


    fun editProfile(
        auth: String,
        name: String,
        gender: String,
        age: Int,
        height: Int,
        weight: Int
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = repository.editProfile(
                        auth, name, gender, age, height, weight
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = "Mac Or Code are wrong"))
        }
    }

}