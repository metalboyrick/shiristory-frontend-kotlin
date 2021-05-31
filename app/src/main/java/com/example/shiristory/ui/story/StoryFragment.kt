package com.example.shiristory.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.example.shiristory.service.story.models.StoryListEntry
import com.example.shiristory.ui.timeline.PostAdapter

class StoryFragment : Fragment() {

    private val _page: Int = 1
    private val _model: StoryViewModel by viewModels()
    private lateinit var _recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val model: StoryViewModel by viewModels()
        val root = inflater.inflate(R.layout.fragment_story, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val storyList: ArrayList<StoryListEntry> = ArrayList<StoryListEntry>()
//
//        storyList.add(StoryListEntry("1","Omni man", "one upon a time, a tale of hero begins", "10 mins ago"))
//        storyList.add(StoryListEntry("2","Omni man 2", "one upon a time, a tale of hero begins", "10 mins ago"))
//        storyList.add(StoryListEntry("3","Omni man 3", "one upon a time, a tale of hero begins", "10 mins ago"))

        _recyclerView = view.findViewById(R.id.story_list_recyclerview)
        _recyclerView.layoutManager = LinearLayoutManager(context)

        _model.getGroups(_page, cache = true)?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                _recyclerView.adapter = StoryListAdapter(it, _model)
            }
        })

    }

}