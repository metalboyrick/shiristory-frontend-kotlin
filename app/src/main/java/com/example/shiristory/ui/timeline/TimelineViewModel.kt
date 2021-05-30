package com.example.shiristory.ui.timeline

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.timeline.TimelineApiService
import com.example.shiristory.service.timeline.models.Comment
import com.example.shiristory.service.timeline.models.Post
import com.example.shiristory.service.timeline.models.PostsResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TimelineViewModel(private val _size: Int = 10) : ViewModel() {

    private val TAG: String = this.javaClass.name

    private var _postPageMap: HashMap<Int, ArrayList<Post>> = HashMap<Int, ArrayList<Post>>()
    private var _postResponse: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()
    private var _addCommentResponse: MutableLiveData<Comment> = MutableLiveData<Comment>()

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
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                if (response.body() != null) {
                    _postResponse.value = response.body()?.posts
                    _postPageMap[page] = response.body()?.posts!!
                }
            }

        })
    }

    fun addComment(post_id: String, comment: String): LiveData<Comment> {
        val data = mapOf("comment" to comment)

        val call: Call<Comment> =
            _service.addComment(post_id = post_id, json_body = Gson().toJson(data))

        call.enqueue(object : Callback<Comment> {

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(
                call: Call<Comment>,
                response: Response<Comment>
            ) {
                _addCommentResponse.value = response.body()
            }
        })

        return _addCommentResponse
    }

    fun likePost(post_id: String, dislike: Boolean = false) {
        val call: Call<GenericResponse> =
            if (!dislike) _service.likePost(post_id = post_id)
            else _service.dislikePost(post_id = post_id)


        call.enqueue(object : Callback<GenericResponse> {

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {
                Log.d(TAG, response.body()?.message!!)
            }
        })
    }
}