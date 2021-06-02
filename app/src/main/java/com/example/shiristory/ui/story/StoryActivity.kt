package com.example.shiristory.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R

class StoryActivity : AppCompatActivity() {

    private var _currentGroupId : String? = ""
    private lateinit var _recyclerView: RecyclerView
    private val _model: StoryViewModel by viewModels()
    private val _page: Int = 1
    private val _size: Int = 6
    private val context = this@StoryActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        // get the group id from the previous clicked item
        _currentGroupId = intent.getStringExtra("groupId")

        _recyclerView = findViewById(R.id.story_recyclerview)
        _recyclerView.layoutManager = LinearLayoutManager(context)

        if(_currentGroupId != null){
            _model.getPostedStories(_page, _size, _currentGroupId!!).observe(this, Observer {
                if (it != null) {
                    _recyclerView.adapter = StoryAdapter(it, _model)
                }
            })
        }

    }
}