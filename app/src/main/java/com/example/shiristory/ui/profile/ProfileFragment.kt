package com.example.shiristory.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.user.models.User
import com.example.shiristory.ui.timeline.PostAdapter
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private val _model: ProfileViewModel by viewModels()
    private var nickname: TextView? = null
    private var profilePic: CircleImageView? = null
    private var bio: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nickname = view.findViewById(R.id.nickname)
        profilePic = view.findViewById(R.id.profile_picture)
        bio = view.findViewById(R.id.bio)

        _model.getUserProfile().observe(viewLifecycleOwner, Observer {
            nickname?.setText(it.nickname)
            bio?.setText(it.bio)
            Glide.with(this).load(it.profile_pic_url).into(profilePic!!)
        })

    }



}