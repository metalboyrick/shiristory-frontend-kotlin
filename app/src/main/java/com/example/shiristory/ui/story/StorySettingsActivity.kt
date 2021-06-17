package com.example.shiristory.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.shiristory.R
import com.example.shiristory.service.common.Constants.FINISHED
import com.example.shiristory.service.common.Constants.ONGOING

class StorySettingsActivity : AppCompatActivity() {

    private var _currentGroupId : String? = ""
    private val _model: StorySettingsViewModel by viewModels()
    private lateinit var groupNameView: EditText
    private lateinit var groupStatusView: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_settings)

        groupNameView = findViewById(R.id.edit_group_name)
        groupStatusView = findViewById(R.id.group_status_switch)

        // get the group id from the previous page
        _currentGroupId = intent.getStringExtra("groupId")

        if(_currentGroupId != null){
            _model.getGroupInfo(_currentGroupId!!).observe(this, Observer {
                if (it != null) {
                    groupNameView.setText(it.name)
                    if(it.status == FINISHED)
                        groupStatusView.isChecked = true
                }
            })
        }
    }
}