package com.example.shiristory.ui.timeline

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.timeline.TimelineApiService
import com.example.shiristory.service.timeline.models.Post
import com.example.shiristory.service.timeline.models.PostsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TimelineViewModel(private val _size: Int = 10) : ViewModel() {

    private var _postPageMap: HashMap<Int, ArrayList<Post>> = HashMap<Int, ArrayList<Post>>()
    private var _postResponse: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()

    private val _service: TimelineApiService = RetrofitBuilder.timelineApiService

    fun getPageSize(): Int {
        return _size
    }

    // We will call this method to get the data
    fun getPosts(page: Int = 1, cache: Boolean = true): LiveData<List<Post>>? {

        if (_postPageMap.containsKey(page) and cache) {
            _postResponse.value = _postPageMap.getValue(page)
        } else {
            _postResponse.value = null
            loadPosts(page)
        }

        return _postResponse
    }

    // This method is using Retrofit to get the JSON data from URL
    private fun loadPosts(page: Int) {
        val call: Call<PostsResponse> = _service.getPosts(page, _size)
        call.enqueue(object : Callback<PostsResponse> {

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                Log.e(this.javaClass.name, t.message!!)
            }

            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                _postResponse.value = response.body()?.posts
                _postPageMap[page] = response.body()?.posts!!
            }

        })
    }
}