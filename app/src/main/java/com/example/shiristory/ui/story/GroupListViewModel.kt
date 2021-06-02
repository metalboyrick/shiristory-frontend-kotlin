package com.example.shiristory.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.story.models.GroupListEntry
import com.example.shiristory.service.story.models.GroupListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupListViewModel : ViewModel() {
    private val TAG: String = this.javaClass.name
    private var _getGroupList: MutableLiveData<List<GroupListEntry>> = MutableLiveData<List<GroupListEntry>>()
    private val _service: StoryApiService = RetrofitBuilder.storyApiService

    fun getGroups(page: Int = 1, size: Int = 3,cache: Boolean = true): LiveData<List<GroupListEntry>> {

        val call: Call<GroupListResponse> = _service.getGroupList(page, size)
        call.enqueue(object : Callback<GroupListResponse> {

            override fun onFailure(call: Call<GroupListResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<GroupListResponse>, response: Response<GroupListResponse>) {
                if (response.body() != null) {
                    _getGroupList.value = response.body()?.groups
                }
            }

        })

        return _getGroupList
    }

}