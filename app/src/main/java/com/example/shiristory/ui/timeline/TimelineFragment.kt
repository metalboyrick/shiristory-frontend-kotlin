package com.example.shiristory.ui.timeline

import android.os.Bundle
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

        val recyclerView: RecyclerView = view.findViewById(R.id.timeline_recyclerview)


        recyclerView.layoutManager = LinearLayoutManager(context)

        _model.getPosts(_page)?.observe(viewLifecycleOwner, Observer {
            print(it)
            postList = it
            recyclerView.adapter = PostAdapter(postList)
        })
    }
}