package com.example.shiristory.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.user.models.User
import com.example.shiristory.ui.auth.LoginActivity
import com.example.shiristory.ui.timeline.PostAdapter
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private val _model: ProfileViewModel by viewModels()
    private var nickname: TextView? = null
    private var profilePic: CircleImageView? = null
    private var bio: TextView? = null
    private var editProfileButton: Button? = null
    private var logoutButton: Button? = null
    private var _user : User? = null

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
        editProfileButton = view.findViewById(R.id.edit_profile)
        logoutButton = view.findViewById(R.id.logout)

        // add listener, that invokes edit profile activity and provide starting values when edit profile is pressed
        editProfileButton?.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            intent.putExtra("nickname",_user?.nickname)
            intent.putExtra("bio",_user?.bio)
            intent.putExtra("profile_pic_url",_user?.profile_pic_url)
            startActivity(intent)
        }

        // client side logout
        logoutButton?.setOnClickListener {
            val sharedPref: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPref.edit()

            // remove stored JWT tokens in shared preference
            editor.remove(R.string.jwt_access_key.toString())
            editor.remove(R.string.jwt_refresh_key.toString())
            editor.apply()

            // go to login page
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        // get profile data
        _model.getUserProfile().observe(viewLifecycleOwner, Observer {
            if(it != null){
                nickname?.setText(it.nickname)
                bio?.setText(it.bio)
                Glide.with(this).load(it.profile_pic_url).into(profilePic!!)
            }
        })

    }


    override fun onResume() {
        super.onResume()

        _model.getUserProfile().observe(viewLifecycleOwner, Observer {
            if(it != null) {
                nickname?.setText(it.nickname)
                bio?.setText(it.bio)
                Glide.with(this).load(it.profile_pic_url).into(profilePic!!)
                _user = it
            }
        })
    }



}