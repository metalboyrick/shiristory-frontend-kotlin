package com.example.shiristory.ui.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.common.MediaType
import com.example.shiristory.service.story.models.StoryEntry

class StoryAdapter(
    private val _dataSet: ArrayList<StoryEntry>,
    private val _model: StoryViewModel
) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val TAG = this.javaClass.name

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val storyContent: TextView = view.findViewById(R.id.story_text_content)
        val storyAuthor: TextView = view.findViewById(R.id.story_author)
        val storyImage: ImageView = view.findViewById(R.id.image_content)
        val storyVideo: VideoView = view.findViewById(R.id.video_content)
        val playAudioBtn: Button = view.findViewById(R.id.audio_content)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StoryViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.story_part_item, viewGroup, false)

        return StoryViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: StoryViewHolder, position: Int) {
        val storyEntry: StoryEntry = _dataSet[position]
        // Get element from your _dataSet at this position and replace the
        // contents of the view with that element

        viewHolder.storyAuthor.text = "By: ${storyEntry.author}"

        when (storyEntry.type) {
            // handle text
            MediaType.TEXT.id -> {
                viewHolder.storyContent.text = storyEntry.content
            }
            // handle images
            MediaType.IMAGE.id -> {
                viewHolder.storyContent.visibility = View.GONE
                viewHolder.storyImage.visibility = View.VISIBLE

                // load the images
                Glide.with(viewHolder.itemView)
                    .load(storyEntry.content)
                    .into(viewHolder.storyImage)
            }
            // handle audio
            MediaType.AUDIO.id -> {

            }
            // handle video
            MediaType.VIDEO.id -> {

                viewHolder.storyContent.visibility = View.GONE
                viewHolder.storyVideo.visibility = View.VISIBLE

                val mediaController = MediaController(viewHolder.itemView.context)
                val postVideo = viewHolder.storyVideo
                mediaController.setAnchorView(postVideo)
                postVideo.setMediaController(mediaController)
                postVideo.setVideoPath(storyEntry.content)
            }
        }

        val storyId: String = storyEntry.storyId
    }

    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

    fun addItem(newMsg: StoryEntry) {
        _dataSet.add(newMsg)
        notifyDataSetChanged()
    }
}