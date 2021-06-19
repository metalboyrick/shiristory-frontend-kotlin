package com.example.shiristory.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.ui.base.GlideImageLoader
import com.rengwuxian.materialedittext.MaterialEditText
import lv.chi.photopicker.ChiliPhotoPicker
import lv.chi.photopicker.PhotoPickerFragment


class EditProfileActivity : AppCompatActivity(), PhotoPickerFragment.Callback {

    private val _model: ProfileViewModel by viewModels()

    private var profile_picture_preview: ImageView? = null;
    private var new_nickname: MaterialEditText? = null
    private var new_bio: MaterialEditText? = null
    private var new_profile_pic_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        getSupportActionBar()?.setTitle("Edit profile")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        ChiliPhotoPicker.init(
            loader = GlideImageLoader(),
            authority = "lv.chi.sample.fileprovider"
        )

        new_nickname = findViewById(R.id.new_nickname)
        new_bio = findViewById(R.id.new_bio)
        profile_picture_preview = findViewById(R.id.profile_picture)

        val old_nickname = intent.getStringExtra("nickname")
        val old_bio = intent.getStringExtra("bio")
        val old_profile_pic_url = intent.getStringExtra("profile_pic_url")

        new_nickname?.setText(old_nickname)
        new_bio?.setText(old_bio)
        Glide.with(this).load(old_profile_pic_url).into(profile_picture_preview!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    fun selectImage(view: View) {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 5,
            theme = R.style.ChiliPhotoPicker_Light
        ).show(supportFragmentManager, "select-image")

    }


    override fun onImagesPicked(photos: ArrayList<Uri>) {
        new_profile_pic_uri = photos[0]
        Glide.with(this).load(photos[0]).into(profile_picture_preview!!)
    }

    fun submitUserProfileUpdate(view: View) {
        _model.updateUserProfile(
            new_nickname?.getText().toString(),
            new_bio?.getText().toString(),
            new_profile_pic_uri
        ).observe(this, Observer {
            if (it) {
                Log.d("update status", "OK")
                // exit current activity
                finish();
                super.onBackPressed();
            }
        })
    }


}