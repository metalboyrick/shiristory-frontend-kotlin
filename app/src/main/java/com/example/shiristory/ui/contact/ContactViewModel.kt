package com.example.shiristory.ui.contact

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.common.models.APIError
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.user.UserApiService
import com.example.shiristory.service.user.models.User
import com.example.shiristory.service.user.models.UserProfileResponse
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactViewModel : ViewModel() {
    private var _friends: MutableLiveData<ArrayList<User>> = MutableLiveData<ArrayList<User>>();
    private var _addFriendStatus : MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    private var _removeFriendStatus : MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
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

    fun updateStatusWithResponse(_status : MutableLiveData<ArrayList<String>> ,
                                 response : Response<GenericResponse> ){
        val statusCode = response.code().toString()
        var message : String? = null

        if (response.isSuccessful) {
            val addFriendResponse = response.body()
            message = addFriendResponse?.message

        } else {
            if(statusCode == "500"){
                message = "Internal server error"
            }
            else{
                val errorMsg : APIError =
                    Gson().fromJson(response.errorBody()!!.charStream(), APIError::class.java)

                val possible_fields : ArrayList<String> = arrayListOf("message","detail")
                var active_field = "none"

                // check for a field that is not null
                for( field in possible_fields ){

                    val temp = errorMsg.javaClass.getMethod("get" + field.capitalize())(errorMsg)

                    if (temp != null) {
                        active_field = field
                        break
                    }
                }
                if(active_field == "none"){
                    message = "default error message"
                }
                else{
                    Log.d("error",errorMsg.javaClass.getMethod("get" + active_field.capitalize())(errorMsg).toString())

                    // guaranteed to be null safe
                    message = errorMsg.javaClass.getMethod("get" + active_field.capitalize())(errorMsg).toString()
                }

            }

        }
        _status.postValue(arrayListOf(statusCode,message!!))
    }

    fun addFriend(username: String) : LiveData<ArrayList<String>>{
        val call: Call<GenericResponse> = _service.addFriend(friend_username = username)
        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.d("user", t.message!!)
            }

            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {

                Log.d("add friend status code", response.code().toString())

                updateStatusWithResponse(_addFriendStatus,response)


            }
        })

        return _addFriendStatus
    }

    fun removeFriend(friendId : String) : LiveData<ArrayList<String>>{
        val call: Call<GenericResponse> = _service.removeFriend(friend_id = friendId)
        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.d("user", t.message!!)
            }

            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {

                Log.d("remove friend status code", response.code().toString())

                updateStatusWithResponse(_removeFriendStatus,response)


            }
        })

        return _removeFriendStatus
    }
}