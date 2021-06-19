package com.example.shiristory.ui.story

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import de.hdodenhof.circleimageview.CircleImageView

class SelectFriendListAdapter(
    private val dataSet: ArrayList<CreateGroupActivity.DataModel>,
    private val mContext: Context
) :
    ArrayAdapter<Any?>(mContext, R.layout.select_friend_item, dataSet as List<Any?>) {

    private class ViewHolder {
        lateinit var txtName: TextView
        lateinit var checkBox: CheckBox
        lateinit var profilePic: CircleImageView
    }

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(position: Int): CreateGroupActivity.DataModel {
        return dataSet[position] as CreateGroupActivity.DataModel
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        val result: View

        if (convertView == null) {
            viewHolder =
                ViewHolder()
            convertView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.select_friend_item, parent, false)
            viewHolder.txtName =
                convertView.findViewById(R.id.friend_nickname)
            viewHolder.checkBox =
                convertView.findViewById(R.id.friend_checkbox)
            viewHolder.profilePic =
                convertView.findViewById(R.id.friend_profile_picture)

            result = convertView
            convertView.tag = viewHolder
        }

        else {
            viewHolder = convertView.tag as ViewHolder
            result = convertView
        }

        val item: CreateGroupActivity.DataModel = getItem(position)
        viewHolder.txtName.text = item.user.nickname
        viewHolder.checkBox.isChecked = item.checked
        Glide.with(mContext).load(item.user.profile_pic_url)
            .into(viewHolder.profilePic)

        return result
    }
}