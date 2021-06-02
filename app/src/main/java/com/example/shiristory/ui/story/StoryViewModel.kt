package com.example.shiristory.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.story.models.StoryEntry
import com.example.shiristory.service.story.models.StoryEntryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private val TAG: String = this.javaClass.name
    private var _getStoryEntries: MutableLiveData<List<StoryEntry>> = MutableLiveData<List<StoryEntry>>()
    private val _service: StoryApiService = RetrofitBuilder.storyApiService

    fun getPostedStories(page: Int = 1, size: Int = 3,groupId: String): LiveData<List<StoryEntry>> {

        val call: Call<StoryEntryResponse> = _service.getPostedStories(groupId, page, size)
        call.enqueue(object : Callback<StoryEntryResponse> {

            override fun onFailure(call: Call<StoryEntryResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<StoryEntryResponse>, response: Response<StoryEntryResponse>) {
                if (response.body() != null) {
                    _getStoryEntries.value = response.body()?.stories
                }
            }

        })

        return _getStoryEntries
    }

}