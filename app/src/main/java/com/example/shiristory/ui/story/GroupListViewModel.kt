package com.example.shiristory.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.story.models.GroupListEntry
import com.example.shiristory.service.story.models.GroupListResponse
import com.example.shiristory.service.timeline.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupListViewModel : ViewModel() {
    private val TAG: String = this.javaClass.name

    private var _groupPageMap: HashMap<Int, ArrayList<GroupListEntry>> = HashMap<Int, ArrayList<GroupListEntry>>()
    private var _getGroupList: MutableLiveData<List<GroupListEntry>> = MutableLiveData<List<GroupListEntry>>()
    private val _service: StoryApiService = RetrofitBuilder.storyApiService

    fun getGroupsByPage(page: Int = 1, size: Int = 3, cache: Boolean = true): LiveData<List<GroupListEntry>>{
        if (_groupPageMap.containsKey(page) and cache) {
            _getGroupList.value = _groupPageMap.getValue(page)
        } else {
            _getGroupList.value = null
            getGroups(page, size)
        }

        return _getGroupList
    }

    fun getGroups(page: Int = 1, size: Int = 3): LiveData<List<GroupListEntry>> {

        val call: Call<GroupListResponse> = _service.getGroupList(page, size)
        call.enqueue(object : Callback<GroupListResponse> {

            override fun onFailure(call: Call<GroupListResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<GroupListResponse>, response: Response<GroupListResponse>) {
                if (response.body() != null) {
                    _groupPageMap[page] = response.body()?.groups!!
                    _getGroupList.value = response.body()?.groups
                }
            }

        })

        return _getGroupList
    }

}