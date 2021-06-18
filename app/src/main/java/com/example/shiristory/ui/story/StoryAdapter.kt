package com.example.shiristory.ui.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.example.shiristory.service.story.models.StoryEntry

class StoryAdapter(private val _dataSet: List<StoryEntry>, private val _model: StoryViewModel) :
        RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val TAG = this.javaClass.name

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val storyContent: TextView = view.findViewById(R.id.story_text_content)
        val storyAuthor: TextView = view.findViewById(R.id.story_author)

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
        val StoryEntry: StoryEntry = _dataSet[position]
        // Get element from your _dataSet at this position and replace the
        // contents of the view with that element
        viewHolder.storyContent.text = StoryEntry.content
        viewHolder.storyAuthor.text = "By: ${StoryEntry.author}"
//        viewHolder.storyLastEdited.text = StoryEntry.lastEdited
        val storyId: String = StoryEntry.storyId
    }

    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

}