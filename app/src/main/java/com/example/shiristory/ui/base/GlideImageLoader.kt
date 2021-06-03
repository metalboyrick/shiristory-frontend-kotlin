package com.example.shiristory.ui.base

import com.example.shiristory.R
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

import lv.chi.photopicker.loader.ImageLoader

class GlideImageLoader: ImageLoader {

    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.image_loader_placeholder)
            .centerCrop()
            .into(view)
    }
}