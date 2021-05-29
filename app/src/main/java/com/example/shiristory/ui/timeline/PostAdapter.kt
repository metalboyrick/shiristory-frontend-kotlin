package com.example.shiristory.ui.timeline

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.timeline.models.Post
import com.like.LikeButton
import com.like.OnLikeListener
import de.hdodenhof.circleimageview.CircleImageView


class PostAdapter(private val dataSet: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val TAG = this.javaClass.name

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postContent: TextView = view.findViewById(R.id.post_content)
        val postAuthor: TextView = view.findViewById(R.id.post_author)
        val postAuthorPic: CircleImageView = view.findViewById(R.id.post_author_pic)
        val postCreatedAt: TextView = view.findViewById(R.id.post_created_at)
        val postLikeButton: LikeButton = view.findViewById(R.id.post_like_button)
        val postLikeCount: TextView = view.findViewById(R.id.post_like_count)
        val postCommentButton: LikeButton = view.findViewById(R.id.post_comment_button)
        val postCommentCount: TextView = view.findViewById(R.id.post_comment_count)
        val postCommentLayout: LinearLayout = view.findViewById(R.id.post_comments_layout)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_item_plain_text, viewGroup, false)

        val commentView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_item_comment, viewGroup, false)

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
        viewHolder.postCommentCount.text = post.comments.size.toString()

        viewHolder.postCommentLayout.removeAllViews()

        for (comment in post.comments) {
            val commentView = LayoutInflater.from(viewHolder.postCommentLayout.context)
                .inflate(R.layout.post_item_comment, viewHolder.postCommentLayout, false)

            commentView.findViewById<TextView>(R.id.post_comment_author).text =
                comment.author.username
            commentView.findViewById<TextView>(R.id.post_comment_created_at).text =
                comment.createdAt
            commentView.findViewById<TextView>(R.id.post_comment_comment).text = comment.comment

            viewHolder.postCommentLayout.addView(commentView)
        }

        viewHolder.postLikeButton.setOnLikeListener(
            object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    Log.d(TAG, "like " + post.content)
                }

                override fun unLiked(likeButton: LikeButton) {
                    Log.d(TAG, "unlike" + post.content)
                }
            }
        )

        viewHolder.postCommentButton.setOnLikeListener(
            object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    viewHolder.postCommentLayout.visibility = View.VISIBLE
                }

                override fun unLiked(likeButton: LikeButton) {
                    viewHolder.postCommentLayout.visibility = View.GONE
                }
            }
        )

        Glide.with(viewHolder.itemView).load(post.author.profilePicUrl)
            .into(viewHolder.postAuthorPic);

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}