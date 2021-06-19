package com.example.shiristory.ui.story

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.common.MediaType
import com.google.gson.Gson

class SendPreviewActivity : AppCompatActivity() {

    private var _currentGroupId: String? = ""
    private var _currentGroupName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_preview)

        val imagePreview: ImageView = findViewById(R.id.story_preview_image)
        val videoPreview: VideoView = findViewById(R.id.story_preview_media_controller)
        val sendBtn: Button = findViewById(R.id.story_preview_send)
        val cancelBtn: Button = findViewById(R.id.story_preview_cancel)

        imagePreview.visibility = View.GONE
        videoPreview.visibility = View.GONE

        val previewIntent = intent
        val type = previewIntent.getIntExtra("type", 1)
        val URI = previewIntent.getStringExtra("URI")
        _currentGroupId = intent.getStringExtra("groupId")
        _currentGroupName = intent.getStringExtra("groupName")

        cancelBtn.setOnClickListener {
            finish()
        }

        sendBtn.setOnClickListener {

            val sendMediaIntent = Intent()
            setResult(Activity.RESULT_OK, sendMediaIntent)
            finish()
        }

        if (type == MediaType.IMAGE.id){

            imagePreview.visibility = View.VISIBLE

            // load the images
            Glide.with(this)
                .load(URI)
                .into(imagePreview)
        } else if ((type == MediaType.VIDEO.id) or (type == MediaType.AUDIO.id )){
            videoPreview.visibility = View.VISIBLE

            val mediaController = MediaController(this)
            val postVideo = videoPreview
            mediaController.setAnchorView(postVideo)
            postVideo.setMediaController(mediaController)
            postVideo.setVideoPath(URI)
        }

    }

    override fun onBackPressed() {
        val storyIntent = Intent(applicationContext, StoryActivity::class.java).apply {
            putExtra("groupId", _currentGroupId)
            putExtra("groupName", _currentGroupName)
        }
        startActivity(storyIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                val storyIntent = Intent(applicationContext, StoryActivity::class.java).apply {
                    putExtra("groupId", _currentGroupId)
                    putExtra("groupName", _currentGroupName)
                }
                startActivity(storyIntent)
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }


}
