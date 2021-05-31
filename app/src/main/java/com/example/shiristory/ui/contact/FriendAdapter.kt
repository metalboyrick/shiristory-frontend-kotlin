package com.example.shiristory.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.user.models.User
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapter(private val _dataSet: List<User>, private val _model: ContactViewModel) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>()
{
    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friend_profile_picture: CircleImageView = view.findViewById(R.id.friend_profile_picture)
        val friend_nickname: TextView = view.findViewById(R.id.friend_nickname)
    }
    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FriendAdapter.FriendViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.friend_item, viewGroup, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: FriendAdapter.FriendViewHolder, position: Int) {
        val friend : User = _dataSet[position]
        Glide.with(viewHolder.itemView).load(friend.profile_pic_url).into(viewHolder.friend_profile_picture)
        viewHolder.friend_nickname.text = friend.nickname
    }

}