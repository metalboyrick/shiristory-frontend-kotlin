package com.example.shiristory.ui.contact

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.user.UserApiService
import com.example.shiristory.service.user.models.User
import com.example.shiristory.service.user.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactViewModel : ViewModel() {
    private var _friends: MutableLiveData<ArrayList<User>> = MutableLiveData<ArrayList<User>>();
    private var _addFriendStatus : MutableLiveData<Int> = MutableLiveData<Int>()
    private val _service: UserApiService = RetrofitBuilder.userApiService

    // We will call this method to get the data
    fun getFriends(): LiveData<ArrayList<User>> {

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

                _friends.value = userProfileResponse?.user?.friends

            }
        })

        return _friends
    }

    fun addFriend(username : String) : LiveData<Int>{
        val call: Call<GenericResponse> = _service.addFriend(friend_username = username)
        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.d("user", t.message!!)
            }

            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {
                val addFriendResponse = response.body()

                _addFriendStatus.value = response.code()

                Log.d("add friend message", response.code().toString())

            }
        })

        return _addFriendStatus
    }

}