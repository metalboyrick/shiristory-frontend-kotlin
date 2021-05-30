package com.example.shiristory.ui.timeline

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.example.shiristory.service.timeline.models.Post

class TimelineFragment : Fragment() {

    private val _model: TimelineViewModel by viewModels()
    private val _page: Int = 1
    private lateinit var _recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_timeline, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var postList: List<Post>

        _recyclerView = view.findViewById(R.id.timeline_recyclerview)


        _recyclerView.layoutManager = LinearLayoutManager(context)

        _model.getPosts(_page)?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                _recyclerView.adapter = PostAdapter(it, _model)
            }
        })
    }
}