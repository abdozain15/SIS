package com.sedra.sis.view.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sedra.sis.data.DataRepository
import com.sedra.sis.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {


    fun editProfile(
        auth: String,
        id: Int,
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
                        auth, id, name, gender, age, height, weight
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = "Mac Or Code are wrong"))
        }
    }


    fun sendOpinion(auth: String, id: Int, body: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.sendOpinion(auth, id, body)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = "Mac Or Code are wrong"))
        }
    }

    fun updateImage(auth: String, id: Int, imagePath: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val userId: RequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                id.toString()
            )
            val file = java.io.File(imagePath)
            val requestBody: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
            val fileToUpload: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", file.name, requestBody)
            emit(Resource.success(data = repository.updateImage(auth, userId, fileToUpload)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = "Mac Or Code are wrong"))
        }
    }
}