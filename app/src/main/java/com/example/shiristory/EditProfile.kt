package com.example.shiristory

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shiristory.ui.base.GlideImageLoader
import lv.chi.photopicker.ChiliPhotoPicker
import lv.chi.photopicker.PhotoPickerFragment


class EditProfile : AppCompatActivity(), PhotoPickerFragment.Callback{
    private var profile_picture_preview : ImageView? = null;
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

        profile_picture_preview = findViewById(R.id.profile_picture_preview)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
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
        ).show(supportFragmentManager,"select-image")

    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        Glide.with(this).load(photos[0]).into(profile_picture_preview!!)
    }

}