package com.example.shiristory.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.common.ToolKits.Companion.parseMultiPartFile
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.user.UserApiService
import com.example.shiristory.service.user.models.User
import com.example.shiristory.service.user.models.UserProfileResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel : ViewModel() {
    private var _profile: MutableLiveData<User> = MutableLiveData<User>();
    private var _updateStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _service: UserApiService = RetrofitBuilder.userApiService

    // We will call this method to get the data
    fun getUserProfile(): LiveData<User> {

        _profile.value = null

        val call: Call<UserProfileResponse> = _service.getUserProfile()
        call.enqueue(object : Callback<UserProfileResponse> {

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.d("user", t.message!!)
            }

            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                val userProfileResponse = response.body()
                Log.d("user", "profile data received")

                Log.d("user", userProfileResponse?.user?.nickname.toString())

                _profile.value = userProfileResponse?.user

            }
        })

        return _profile
    }


    fun updateUserProfile(
        new_nickname: String,
        new_bio: String,
        new_profile_pic_uri: Uri? = Uri.EMPTY
    ): LiveData<Boolean> {
        Log.d("new_nickname", new_nickname)
        Log.d("new_bio", new_bio)
        Log.d("new_profile_pic_uri", new_profile_pic_uri!!.getPath()!!)

        val call: Call<GenericResponse>
        if(new_profile_pic_uri == Uri.EMPTY){
            call = _service.updateUserProfile(
                new_nickname = RequestBody.create(MediaType.parse("multipart/form-data"), new_nickname),
                new_bio = RequestBody.create(MediaType.parse("multipart/form-data"), new_bio)
            )
        }
        else{
            val profile_image: MultipartBody.Part = parseMultiPartFile(new_profile_pic_uri!!, "image/*")

            call = _service.updateUserProfile(
                new_nickname = RequestBody.create(MediaType.parse("multipart/form-data"), new_nickname),
                new_bio = RequestBody.create(MediaType.parse("multipart/form-data"), new_bio),
                new_profile_pic = profile_image
            )
        }
        _updateStatus.value = false

        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.d("user", t.message!!)
            }

            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {
                val userProfileResponse = response.body()
                Log.d("user", "profile updated")
                Log.d("update message", userProfileResponse?.message!!)
                _updateStatus.value = true

            }
        })

        return _updateStatus
    }

}