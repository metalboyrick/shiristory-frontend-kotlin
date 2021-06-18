package com.example.shiristory.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.story.models.GroupInfoResponse
import com.example.shiristory.service.story.models.GroupListEntry
import com.example.shiristory.service.story.models.GroupListResponse
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

    // TODO: leave group (Wait for user state implementation)

}