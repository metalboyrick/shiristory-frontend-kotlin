package com.example.shiristory.ui.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.timeline.TimelineApiService
import com.example.shiristory.service.timeline.models.Post


class TimelineViewModel : ViewModel() {

    private var _postList: MutableLiveData<List<Post>>? = null

    private val _service:TimelineApiService = RetrofitBuilder.timelineApiService

}