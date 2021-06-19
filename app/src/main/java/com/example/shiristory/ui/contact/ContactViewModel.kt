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
import com.example.shiristory.service.user.models.SearchFriendResponse
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
    private var _searchFriendStatus : MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    private val _service: UserApiService = RetrofitBuilder.userApiService

    // We will call this method to get the data
    fun getFriends(): LiveData<ArrayList<User>> {
        _friends.value = null;

        val call: Call<UserProfileResponse> = _service.getUserProfile()
        call.enqueue(object : Callback<UserProfileResponse> {

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.d("get friends", t.message!!)
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
        var message : String? = "Default error message"

        if (response.isSuccessful) {
            val addFriendResponse = response.body()
            message = addFriendResponse?.message

        }
        else {
            if(statusCode == "500"){
                message = "Internal server error"
            }
            // try to find a field containing errors
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

        // update mutable live data and notify all observers using postValue
        _status.postValue(arrayListOf(statusCode,message!!))
    }

    fun addFriend(username: String) : LiveData<ArrayList<String>>{

        _addFriendStatus.value = null

        val call: Call<GenericResponse> = _service.addFriend(friend_username = username)
        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.d("add friend", t.message!!)
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

        _removeFriendStatus.value = null

        val call: Call<GenericResponse> = _service.removeFriend(friend_id = friendId)
        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.d("remove friend", t.message!!)
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

    fun searchFriend(friendNickname : String) : LiveData<ArrayList<User>>{

        _friends.value = null

        val call: Call<SearchFriendResponse> = _service.searchFriend(friend_nickname = friendNickname)
        call.enqueue(object : Callback<SearchFriendResponse> {

            override fun onFailure(call: Call<SearchFriendResponse>, t: Throwable) {
                Log.d("search friend", t.message!!)
            }

            override fun onResponse(
                call: Call<SearchFriendResponse>,
                response: Response<SearchFriendResponse>
            ) {

                val searchFriendResponse = response.body()
                Log.d("user", "search friend data received")

                _friends.value = searchFriendResponse?.friends


            }
        })
        return _friends
    }
}