package com.example.shiristory.ui.story

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.MainActivity
import com.example.shiristory.R
import com.example.shiristory.service.common.Constants.FINISHED
import com.example.shiristory.service.common.Constants.ONGOING

class StorySettingsActivity : AppCompatActivity() {

    private var _currentGroupId : String? = ""
    private val _model: StorySettingsViewModel by viewModels()
    private lateinit var _groupNameView: EditText
    private lateinit var _groupStatusView: Switch
    private lateinit var _leaveGroupBtn: Button
    private var _currentUsername: String? = ""
    private var _currentUserId: String? = ""
    private val _context = this@StorySettingsActivity
    private lateinit var _memberRecyclerView: RecyclerView
    private lateinit var _adminRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_settings)

        _groupNameView = findViewById(R.id.edit_group_name)
        _groupStatusView = findViewById(R.id.group_status_switch)
        _leaveGroupBtn = findViewById(R.id.group_leave_button)
        _memberRecyclerView = findViewById(R.id.group_members_recyclerview)
        _adminRecyclerView = findViewById(R.id.group_admins_recyclerview)

        // get the group id from the previous page
        _currentGroupId = intent.getStringExtra("groupId")

        val sharedPref: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)

        _currentUserId = sharedPref.getString("userId", " ")

        // inject linear layout manager
        _memberRecyclerView.layoutManager = LinearLayoutManager(_context)
        _adminRecyclerView.layoutManager = LinearLayoutManager(_context)

        // fill in the information
        if(_currentGroupId != null){
            _model.getGroupInfo(_currentGroupId!!).observe(this, Observer {
                if (it != null) {
                    _groupNameView.setText(it.name)
                    if(it.status == FINISHED)
                        _groupStatusView.isChecked = true
                    _memberRecyclerView.adapter = MemberListAdapter(it.members, _model, it.admins)
                    _adminRecyclerView.adapter = AdminListAdapter(it.admins, _model)
                }
            })
        }

        // leave group button
        _leaveGroupBtn.setOnClickListener {
            _model.leaveGroup(_currentUserId!!, _currentGroupId!!)
            val goToMainIntent = Intent(this, MainActivity::class.java)
            startActivity(goToMainIntent)
        }


    }
}