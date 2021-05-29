package com.example.shiristory.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.user.UserApiService
import com.example.shiristory.service.user.models.User
import com.example.shiristory.service.user.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private var _profile: MutableLiveData<User> = MutableLiveData<User>();
    private val _service: UserApiService = RetrofitBuilder.userApiService

    // We will call this method to get the data
    fun getUserProfile(): LiveData<User> {

        loadUserProfile()

        return _profile
    }

    fun loadUserProfile() {
        val call: Call<UserProfileResponse> = _service.getUserProfile()
        call.enqueue(object : Callback<UserProfileResponse> {

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.d("user", t.message!!)
            }

            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                val userProfileResponse = response.body()
                Log.d("user","profile data received")

                Log.d("user", userProfileResponse?.user?.nickname.toString())

                _profile.value = userProfileResponse?.user

            }
        })
    }

}