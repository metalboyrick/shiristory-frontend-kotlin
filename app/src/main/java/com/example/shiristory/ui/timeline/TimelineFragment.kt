package com.example.shiristory.ui.timeline

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.example.shiristory.service.common.RequestCodes
import com.example.shiristory.service.timeline.models.Post
import com.google.gson.Gson


class TimelineFragment : Fragment() {
    private val TAG = this.javaClass.name
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
                Log.d(TAG, "Post loaded.")
                _recyclerView.adapter = PostAdapter(ArrayList(it), _model)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.timeline_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_add_post -> {
                val intent = Intent(activity, AddPostActivity::class.java)
                startActivityForResult(intent, RequestCodes.REQUEST_ADD_POST)
                activity?.overridePendingTransition(R.anim.slide_in_bottom, R.anim.hold)
            }

            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, requestCode.toString())
        Log.d(TAG, resultCode.toString())
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodes.REQUEST_ADD_POST -> {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        (_recyclerView.adapter as PostAdapter).addPost(
                            Gson().fromJson(
                                data.getStringExtra(
                                    "post"
                                ), Post::class.java
                            )
                        )
                        _recyclerView.smoothScrollToPosition(0)
                    }
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}