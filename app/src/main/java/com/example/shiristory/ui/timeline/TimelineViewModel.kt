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


class TimelineViewModel : ViewModel() {

    private var _postList: MutableLiveData<List<Post>>? = null

    private val _service: TimelineApiService = RetrofitBuilder.timelineApiService

    // We will call this method to get the data
    fun getPosts(page:Int = 1, size:Int = 10): LiveData<List<Post>>? {

        // If the list is null
        if (_postList == null) {
            _postList = MutableLiveData<List<Post>>()
            // We will load it asynchronously from server in this method
            loadPosts(page, size)
        }

        return _postList
    }

    // This method is using Retrofit to get the JSON data from URL
    private fun loadPosts(page: Int = 1, size: Int = 10) {
        val call: Call<PostsResponse> = _service.getPosts(page, size)
        call.enqueue(object : Callback<PostsResponse> {

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                Log.d("we", t.message!!)
            }

            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                val postsResponse = response.body()
                Log.d("we","aaaaaaa")
                _postList?.value = postsResponse?.posts
            }
        })
    }
}