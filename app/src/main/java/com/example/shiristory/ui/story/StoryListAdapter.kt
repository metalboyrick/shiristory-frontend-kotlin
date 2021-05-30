package com.example.shiristory.ui.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.example.shiristory.service.timeline.models.StoryListEntry


class StoryListAdapter(private val _dataSet: List<StoryListEntry>) :
    RecyclerView.Adapter<StoryListAdapter.StoryListViewHolder>() {

    private val TAG = this.javaClass.name

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class StoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val storyName: TextView = view.findViewById(R.id.story_name)
        val storySummary: TextView = view.findViewById(R.id.story_summary)
        val storyLastEdited: TextView = view.findViewById(R.id.story_last_edited)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StoryListViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.story_item, viewGroup, false)

        return StoryListViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: StoryListViewHolder, position: Int) {
        val storyListEntry: StoryListEntry = _dataSet[position]
        // Get element from your _dataSet at this position and replace the
        // contents of the view with that element
        viewHolder.storyName.text = storyListEntry.name
        viewHolder.storySummary.text = storyListEntry.summary
        viewHolder.storyLastEdited.text = storyListEntry.lastEdited

    }

    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

}