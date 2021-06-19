package com.example.shiristory.ui.story

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shiristory.R
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.story.models.GroupCreateResponse
import com.example.shiristory.service.user.UserApiService
import com.example.shiristory.service.user.models.User
import com.example.shiristory.service.user.models.UserProfileResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateGroupActivity : AppCompatActivity() {

    private val TAG = this.javaClass.name
    private lateinit var dataModel: ArrayList<DataModel>
    private lateinit var listView: ListView
    private lateinit var createGroupButton: Button
    private var currentUserId: String? = null
    private lateinit var adapter: SelectFriendListAdapter
    private val _userApiService: UserApiService = RetrofitBuilder.userApiService


    class DataModel internal constructor(var user: User, var checked: Boolean = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        listView = findViewById(R.id.friend_list)

        createGroupButton = findViewById(R.id.create_group_button)
        createGroupButton.setOnClickListener {
            createGroup()
        }

        val call = _userApiService.getUserProfile()

        call.enqueue(object : Callback<UserProfileResponse> {

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.d("TAG", t.message!!)
            }

            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                dataModel = ArrayList<DataModel>()
                val friends: ArrayList<User>? = response.body()?.user?.friends
                currentUserId = response.body()?.user?.id

                if (friends != null) {
                    for (friend in friends) {
                        dataModel.add(DataModel(user = friend))
                    }
                }

                adapter = SelectFriendListAdapter(dataModel, applicationContext)

                listView.adapter = adapter

                listView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        val dataModel: DataModel = dataModel[position]
                        dataModel.checked = !dataModel.checked
                        Log.d("banana", dataModel.checked.toString())
                        adapter.notifyDataSetChanged()
                    }
            }
        })

    }

    private fun createGroup() {

        val inputGroupName: EditText = findViewById(R.id.story_input_grp_name)

        val firstStory = mapOf(
            "story_type" to 0,
            "story_content" to "Story starts here",
            "next_story_type" to 0
        )

        val members = ArrayList<String>()

        // Add self
        members.add(currentUserId!!)

        for (item in dataModel) {
            if (item.checked) {
                members.add(item.user.id)
            }
        }

        if (members.size <= 1) {
            Toast.makeText(this, "Members can't be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        val data = mapOf(
            "group_name" to inputGroupName.text.toString(),
            "group_members" to members,
            "vote_duration" to 30,
            "vote_threshold" to 5,
            "first_story" to firstStory
        )

        val call: Call<GroupCreateResponse> =
            RetrofitBuilder.storyApiService.createStoryGroup(json_body = Gson().toJson(data))

        call.enqueue(object : Callback<GroupCreateResponse> {

            override fun onFailure(call: Call<GroupCreateResponse>, t: Throwable) {
                Log.e(TAG, t.message!!)
            }

            override fun onResponse(
                call: Call<GroupCreateResponse>,
                response: Response<GroupCreateResponse>
            ) {

                if (response.body() != null) {
                    val storyIntent = Intent(applicationContext, StoryActivity::class.java).apply {
                        putExtra("groupId", response.body()?.group_id)
                        putExtra("groupName", inputGroupName.text.toString())
                    }
                    startActivity(storyIntent)
                }
            }
        })
    }
}