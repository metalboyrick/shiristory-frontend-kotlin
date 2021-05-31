package com.example.shiristory.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.story.models.StoryListEntry
import com.example.shiristory.service.story.models.StoryListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private val TAG: String = this.javaClass.name
    private var _getStoryListResponse: MutableLiveData<List<StoryListEntry>> = MutableLiveData<List<StoryListEntry>>()
    private val _service: StoryApiService = RetrofitBuilder.storyApiService

    fun getGroups(page: Int = 1, cache: Boolean = true): LiveData<List<StoryListEntry>> {

        val call: Call<StoryListResponse> = _service.getGroupList(1, 2)
        call.enqueue(object : Callback<StoryListResponse> {

            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<StoryListResponse>, response: Response<StoryListResponse>) {
                if (response.body() != null) {
                    _getStoryListResponse.value = response.body()?.groups
                }
            }

        })

        return _getStoryListResponse
    }

}