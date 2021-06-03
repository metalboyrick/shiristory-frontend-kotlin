package com.example.shiristory.ui.story

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.shiristory.R
import com.example.shiristory.service.story.models.GroupListEntry


class GroupListAdapter(private val _dataSet: List<GroupListEntry>, private val _model: GroupListViewModel) :
    RecyclerView.Adapter<GroupListAdapter.StoryListViewHolder>() {

    private val TAG = this.javaClass.name

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class StoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val storyName: TextView = view.findViewById(R.id.story_name)
        val storySummary: TextView = view.findViewById(R.id.story_summary)
        val storyLastEdited: TextView = view.findViewById(R.id.story_last_edited)
        val storyEntry: CardView = view.findViewById(R.id.story_entry_card)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StoryListViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.group_list_item, viewGroup, false)

        return StoryListViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: StoryListViewHolder, position: Int) {
        val groupListEntry: GroupListEntry = _dataSet[position]
        // Get element from your _dataSet at this position and replace the
        // contents of the view with that element
        viewHolder.storyName.text = groupListEntry.name
        viewHolder.storySummary.text = groupListEntry.summary
        viewHolder.storyLastEdited.text = groupListEntry.lastEdited
        val groupId: String = groupListEntry.groupId

        // navigate to the appropriate story page
        viewHolder.storyEntry.setOnClickListener {
            val storyIntent = Intent(it.context, StoryActivity::class.java).apply{
                putExtra("groupId", groupId)
            }
            startActivity(it.context, storyIntent, null)
        }

    }

    // Return the size of your _dataSet (invoked by the layout manager)
    override fun getItemCount() = _dataSet.size

}