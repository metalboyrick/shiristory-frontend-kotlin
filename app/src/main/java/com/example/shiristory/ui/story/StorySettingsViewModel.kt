package com.example.shiristory.ui.story

import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.R
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.story.models.DeleteMemberRequest
import com.example.shiristory.service.story.models.GroupInfoResponse
import com.example.shiristory.service.story.models.GroupListEntry
import com.example.shiristory.service.story.models.GroupListResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StorySettingsViewModel: ViewModel() {
    private val TAG: String = this.javaClass.name
    private var _getGroupInfo: MutableLiveData<GroupInfoResponse> = MutableLiveData<GroupInfoResponse>()
    private val _service: StoryApiService = RetrofitBuilder.storyApiService

    // get group info API wrapper
    fun getGroupInfo(groupId: String): LiveData<GroupInfoResponse> {
        val call: Call<GroupInfoResponse> = _service.getGroupInfo(groupId)
        call.enqueue(object : Callback<GroupInfoResponse> {

            override fun onFailure(call: Call<GroupInfoResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<GroupInfoResponse>, response: Response<GroupInfoResponse>) {
                if (response.body() != null) {
                    _getGroupInfo.value = response.body()
                }
            }

        })

        return _getGroupInfo
    }

    // TODO: leave group
    fun leaveGroup(memberId: String, groupId:String){

        val targetMember = DeleteMemberRequest(memberId = memberId)

        val call: Call<GenericResponse> = _service.deleteMember(groupId, Gson().toJson(targetMember))
        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.body() != null) {
                    Log.e(TAG, "Delete OK!!!")
                }
            }

        })
    }


}