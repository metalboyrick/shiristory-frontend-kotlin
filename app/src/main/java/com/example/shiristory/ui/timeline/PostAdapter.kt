package com.example.shiristory.ui.timeline

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.common.ToolKits
import com.example.shiristory.service.timeline.models.Comment
import com.example.shiristory.service.timeline.models.Post
import com.like.LikeButton
import com.like.OnLikeListener
import com.rengwuxian.materialedittext.MaterialEditText
import de.hdodenhof.circleimageview.CircleImageView


class PostAdapter(private val _dataSet: ArrayList<Post>, private val _model: TimelineViewModel) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val TAG = this.javaClass.name

    fun addPost(post:Post){
        _dataSet.add(0, post)
        this.notifyItemInserted(0)
    }

    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

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
        val postCommentInputLayout: LinearLayout = view.findViewById(R.id.post_comment_input_layout)
        val postCommentSubmitButton: ImageButton =
            view.findViewById(R.id.post_comment_submit_button)
        val postCommentInput: MaterialEditText = view.findViewById(R.id.post_comment_input)

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
        val post: Post = _dataSet[position]
        // Get element from your _dataSet at this position and replace the
        // contents of the view with that element
        viewHolder.postContent.text = post.content
        viewHolder.postAuthor.text = post.author.username
        viewHolder.postCreatedAt.text = post.createdAt

        // Comments
        viewHolder.postCommentCount.text = post.comments.size.toString()
        viewHolder.postCommentLayout.removeAllViews()
        for (comment in post.comments) {
            addCommentView(viewHolder, comment)
        }
        viewHolder.postCommentButton.setOnLikeListener(
            object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    viewHolder.postCommentLayout.visibility = View.VISIBLE
                    viewHolder.postCommentInputLayout.visibility = View.VISIBLE
                }

                override fun unLiked(likeButton: LikeButton) {
                    viewHolder.postCommentLayout.visibility = View.GONE
                    viewHolder.postCommentInputLayout.visibility = View.GONE
                }
            }
        )

        viewHolder.postCommentSubmitButton.setOnClickListener(View.OnClickListener() {
            submitComment(viewHolder, post)
        })

        // Likes
        viewHolder.postLikeCount.text = post.likes.size.toString()
        // TODO: check if user has already liked
        viewHolder.postLikeButton.setOnLikeListener(
            object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    Log.d(TAG, "like " + post.content)
                    _model.likePost(post.id)
                    updateLikeCount(viewHolder, 1)
                }

                override fun unLiked(likeButton: LikeButton) {
                    Log.d(TAG, "dislike" + post.content)
                    _model.likePost(post.id, dislike = true)
                    updateLikeCount(viewHolder, -1)
                }
            }
        )

        // Load profile pic
        Glide.with(viewHolder.itemView).load(post.author.profilePicUrl)
            .into(viewHolder.postAuthorPic)
    }

    private fun addCommentView(viewHolder: PostViewHolder, comment: Comment) {
        val commentView = LayoutInflater.from(viewHolder.postCommentLayout.context)
            .inflate(R.layout.post_item_comment, viewHolder.postCommentLayout, false)

        commentView.findViewById<TextView>(R.id.post_comment_author).text =
            comment.author.username
        commentView.findViewById<TextView>(R.id.post_comment_created_at).text =
            comment.createdAt
        commentView.findViewById<TextView>(R.id.post_comment_comment).text = comment.comment

        viewHolder.postCommentLayout.addView(commentView)
    }

    private fun submitComment(viewHolder: PostViewHolder, post: Post) {
        val input: MaterialEditText = viewHolder.postCommentInput
        val commentCountView: TextView = viewHolder.postCommentCount
        val text: String = input.text.toString()

        viewHolder.postCommentInput.clearFocus()

        if (text.isNotEmpty()) {

            Log.d(TAG, "clicked: " + post.id + " " + text)

            val context: Context = viewHolder.postCommentLayout.context

            _model.addComment(post_id = post.id, comment = text)
                .observe(context as LifecycleOwner, Observer {
                    if (it != null) {
                        addCommentView(viewHolder, it)
                        commentCountView.text =
                            (commentCountView.text.toString().toInt() + 1).toString()
                        input.text?.clear()
                        ToolKits.hideSoftKeyboard(context, input)
                    }
                })
        }
    }

    private fun updateLikeCount(viewHolder: PostViewHolder, increment: Int) {
        val likeCountView: TextView = viewHolder.postLikeCount
        likeCountView.text = (likeCountView.text.toString().toInt() + increment).toString()
    }
}