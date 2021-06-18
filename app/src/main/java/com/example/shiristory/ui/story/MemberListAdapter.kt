package com.example.shiristory.ui.story

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.story.models.GroupMembersEntry
import com.example.shiristory.ui.base.GlideImageLoader
import de.hdodenhof.circleimageview.CircleImageView

class MemberListAdapter(private val _dataSet: ArrayList<GroupMembersEntry>, private val _model: StorySettingsViewModel) :
    RecyclerView.Adapter<MemberListAdapter.MemberListViewHolder>() {

    private val TAG = this.javaClass.name

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class MemberListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val memberName: TextView = view.findViewById(R.id.friend_nickname)
        val memberPicture: CircleImageView = view.findViewById(R.id.friend_profile_picture)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MemberListViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.friend_item, viewGroup, false)

        return MemberListViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MemberListViewHolder, position: Int) {
        val member: GroupMembersEntry = _dataSet[position]
        // Get element from your _dataSet at this position and replace the
        // contents of the view with that element
        viewHolder.memberName.text = member.nickname

        // load the images
        Glide.with(viewHolder.itemView)
            .load(member.profilePicUrl)
            .into(viewHolder.memberPicture)
    }

    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

}