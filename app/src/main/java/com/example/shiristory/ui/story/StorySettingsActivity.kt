package com.example.shiristory.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.example.shiristory.service.common.Constants.FINISHED
import com.example.shiristory.service.common.Constants.ONGOING

class StorySettingsActivity : AppCompatActivity() {

    private var _currentGroupId : String? = ""
    private val _model: StorySettingsViewModel by viewModels()
    private lateinit var _groupNameView: EditText
    private lateinit var _groupStatusView: Switch
    private val _context = this@StorySettingsActivity
    private lateinit var _recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_settings)

        _groupNameView = findViewById(R.id.edit_group_name)
        _groupStatusView = findViewById(R.id.group_status_switch)
        _recyclerView = findViewById(R.id.group_members_recyclerview)

        // get the group id from the previous page
        _currentGroupId = intent.getStringExtra("groupId")

        // inject linear layout manager
        _recyclerView.layoutManager = LinearLayoutManager(_context)

        // fill in the information
        if(_currentGroupId != null){
            _model.getGroupInfo(_currentGroupId!!).observe(this, Observer {
                if (it != null) {
                    _groupNameView.setText(it.name)
                    if(it.status == FINISHED)
                        _groupStatusView.isChecked = true
                    _recyclerView.adapter = MemberListAdapter(it.members, _model)
                }
            })
        }





    }
}