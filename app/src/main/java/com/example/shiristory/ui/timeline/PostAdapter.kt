package com.example.shiristory.ui.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.shiristory.R
import com.example.shiristory.service.timeline.models.Post
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(private val dataSet: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postContent: TextView = view.findViewById(R.id.post_content)
        val postAuthor: TextView = view.findViewById(R.id.post_author)
        val postAuthorPic: CircleImageView = view.findViewById(R.id.post_author_pic)
        val postCreatedAt: TextView = view.findViewById(R.id.post_created_at)
        val postLikeCount: TextView = view.findViewById(R.id.post_like_count)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_item_plain_text, viewGroup, false)

        return PostViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post: Post = dataSet[position]
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.postContent.text = post.content
        viewHolder.postAuthor.text = post.author.username
        viewHolder.postCreatedAt.text = post.createdAt
        viewHolder.postLikeCount.text = post.likes.size.toString()

        Glide.with(viewHolder.itemView).load(post.author.profilePicUrl)
            .into(viewHolder.postAuthorPic);

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}