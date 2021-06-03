package com.example.shiristory.ui.contact

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.user.models.User
import de.hdodenhof.circleimageview.CircleImageView


class FriendAdapter(private val _dataSet: ArrayList<User>, private val _model: ContactViewModel) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
    var removeFriendAPIisNotObserved: Boolean = true

    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friend_profile_picture: CircleImageView = view.findViewById(R.id.friend_profile_picture)
        val friend_nickname: TextView = view.findViewById(R.id.friend_nickname)
        val remove_friend: TextView = view.findViewById(R.id.remove_friend)
        val friend_item_layout: RelativeLayout = view.findViewById(R.id.friend_item_layout)
        var friend_id: String = ""


    }

    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FriendAdapter.FriendViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.friend_item, viewGroup, false)
        val viewHolder = FriendViewHolder(view)
        viewHolder.remove_friend.setOnClickListener {
            val pos = viewHolder.adapterPosition
            val context: Context = viewHolder.friend_item_layout.context

            Log.d("remove friend", viewHolder.friend_id)
            Log.d("friend index in onclick", pos.toString())

            if (pos != RecyclerView.NO_POSITION) {
                val res: LiveData<ArrayList<String>> = _model.removeFriend(viewHolder.friend_id)
                res.observe(context as LifecycleOwner, Observer {
                    res.removeObservers(context as LifecycleOwner)
                    Log.d("remove friend", "callback called")
                    val statusCode = Integer.parseInt(it[0])
                    val message: String = it[1]
                    if (statusCode == 200) {
                        Log.d(
                            "friend nickname in observer",
                            viewHolder.friend_nickname.text.toString()
                        )

                        _dataSet.removeAt(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(0, getItemCount());
                    }
                })
            }
        }

        return viewHolder
    }


    override fun onBindViewHolder(viewHolder: FriendAdapter.FriendViewHolder, position: Int) {
        val friend: User = _dataSet[position]
        Glide.with(viewHolder.itemView).load(friend.profile_pic_url)
            .into(viewHolder.friend_profile_picture)
        viewHolder.friend_nickname.text = friend.nickname
        viewHolder.friend_id = friend.id

        val friendObserver = Observer<ArrayList<String>> { it ->
        }


    }

}