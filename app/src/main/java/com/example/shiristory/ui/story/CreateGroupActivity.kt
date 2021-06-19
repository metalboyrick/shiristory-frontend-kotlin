package com.example.shiristory.ui.story

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.shiristory.R
import com.example.shiristory.service.common.RetrofitBuilder
import com.example.shiristory.service.user.UserApiService
import com.example.shiristory.service.user.models.User
import com.example.shiristory.service.user.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateGroupActivity : AppCompatActivity() {

    private lateinit var dataModel: ArrayList<DataModel>
    private lateinit var listView: ListView
    private lateinit var adapter: SelectFriendListAdapter
    private val _userApiService: UserApiService = RetrofitBuilder.userApiService


    class DataModel internal constructor(var user: User, var checked: Boolean = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        listView = findViewById(R.id.friend_list)

        val call = _userApiService.getUserProfile()

        call.enqueue(object : Callback<UserProfileResponse> {

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.d("user", t.message!!)
            }

            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                dataModel = ArrayList<DataModel>()
                val friends: ArrayList<User>? = response.body()?.user?.friends

                if (friends != null) {
                    for (friend in friends) {
                        dataModel.add(DataModel(user = friend))
                    }
                }

                listView.adapter = SelectFriendListAdapter(dataModel, applicationContext)

                listView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        val dataModel: DataModel = dataModel[position]
                        dataModel.checked = !dataModel.checked
                        adapter.notifyDataSetChanged()
                    }
            }
        })

    }
}