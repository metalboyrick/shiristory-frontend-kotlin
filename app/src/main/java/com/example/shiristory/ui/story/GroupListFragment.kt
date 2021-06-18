package com.example.shiristory.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupListFragment : Fragment() {

    private val _page: Int = 1
    private val _size: Int = 6
    private val _model: GroupListViewModel by viewModels()
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _createNewGrpBtn: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val model: GroupListViewModel by viewModels()
        val root = inflater.inflate(R.layout.fragment_group_list, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val storyList: ArrayList<StoryListEntry> = ArrayList<StoryListEntry>()
//
//        storyList.add(StoryListEntry("1","Omni man", "one upon a time, a tale of hero begins", "10 mins ago"))
//        storyList.add(StoryListEntry("2","Omni man 2", "one upon a time, a tale of hero begins", "10 mins ago"))
//        storyList.add(StoryListEntry("3","Omni man 3", "one upon a time, a tale of hero begins", "10 mins ago"))


        _createNewGrpBtn = view.findViewById(R.id.create_new_grp_btn)
        _createNewGrpBtn.setOnClickListener {
            val createGroupIntent = Intent(view.context!!, CreateGroupActivity::class.java)
            ContextCompat.startActivity(view.context!!, createGroupIntent , null)
        }

        _recyclerView = view.findViewById(R.id.group_list_recyclerview)
        _recyclerView.layoutManager = LinearLayoutManager(context)

        _model.getGroups(_page, _size,cache = true)?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                _recyclerView.adapter = GroupListAdapter(it, _model)
            }
        })

    }

    fun gotoCreateGroup(view:View){

    }

}