package com.example.shiristory.ui.story

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceManager
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
        val storyCard: CardView = view.findViewById(R.id.chat_card)

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

        val sharedPref: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(viewHolder.itemView.context)

        if (storyEntry.author == sharedPref.getString("username", " ")){
            viewHolder.storyCard.setCardBackgroundColor(viewHolder.itemView.context.getColor(R.color.gray_100))

        } else {
            viewHolder.storyCard.setCardBackgroundColor(viewHolder.itemView.context.getColor(R.color.alice_blue_100))
        }

        Log.d(TAG, sharedPref.getString("username", " ")!!)

        viewHolder.storyAuthor.text = "By: ${storyEntry.author}"

        viewHolder.storyContent.visibility = View.GONE
        viewHolder.storyImage.visibility = View.GONE
        viewHolder.playAudioBtn.visibility = View.GONE
        viewHolder.storyVideo.visibility = View.GONE

        when (storyEntry.type) {

            // handle text
            MediaType.TEXT.id -> {
                viewHolder.storyContent.visibility = View.VISIBLE
                viewHolder.storyContent.text = storyEntry.content

            }

            // handle images
            MediaType.IMAGE.id -> {
                viewHolder.storyImage.visibility = View.VISIBLE

                // load the images
                Glide.with(viewHolder.itemView)
                    .load(storyEntry.content)
                    .into(viewHolder.storyImage)
            }

            // handle audio
            MediaType.AUDIO.id -> {

                // handle mediaplayer logic here
                var parsedUri = Uri.parse(storyEntry.content)
                var mediaPlayer = MediaPlayer.create(viewHolder.itemView.context, parsedUri)

                // listener when the media player has finished playing
                mediaPlayer.setOnCompletionListener {
                    viewHolder.playAudioBtn.text = viewHolder.itemView.context.getString(R.string.play_audio)
                }

                viewHolder.playAudioBtn.setOnClickListener {
                    if (mediaPlayer.isPlaying){
                        mediaPlayer.pause()
                        viewHolder.playAudioBtn.text = viewHolder.itemView.context.getString(R.string.play_audio)
                    }
                    else {
                        mediaPlayer.seekTo(0)
                        mediaPlayer.start()
                        viewHolder.playAudioBtn.text = viewHolder.itemView.context.getString(R.string.stop_audio)
                    }
                }

                viewHolder.playAudioBtn.visibility = View.VISIBLE

            }

            // handle video
            MediaType.VIDEO.id -> {
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