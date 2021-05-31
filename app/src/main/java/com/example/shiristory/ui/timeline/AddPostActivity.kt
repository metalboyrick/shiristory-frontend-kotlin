package com.example.shiristory.ui.timeline

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.shiristory.R
import com.example.shiristory.service.common.MediaType
import com.example.shiristory.service.common.RequestCodes
import com.example.shiristory.service.common.RequestCodes.REQUEST_MEDIA_PICKER_SELECT
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText


class AddPostActivity : AppCompatActivity() {

    private val _model: TimelineViewModel by viewModels()
    private lateinit var _contentInput: MaterialEditText
    private lateinit var _postAddImageView: ImageView
    private lateinit var _postAddVideoView: VideoView
    private var _mediaType: MediaType? = null
    private var _mediaUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val selectMediaButton: Button = findViewById(R.id.post_add_select_media_button)
        val takePhotoButton: ImageButton = findViewById(R.id.post_add_take_photo_button)
        val recordVideoButton: ImageButton = findViewById(R.id.post_add_record_video_button)
        _contentInput = findViewById(R.id.post_add_input)
        _postAddImageView = findViewById(R.id.post_add_image)
        _postAddVideoView = findViewById(R.id.post_add_video)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(_postAddVideoView)
        _postAddVideoView.setMediaController(mediaController)

        selectMediaButton.setOnClickListener(View.OnClickListener() {
            selectMedia()
        })
        takePhotoButton.setOnClickListener(View.OnClickListener() {
            takePhoto()
        })
        recordVideoButton.setOnClickListener(View.OnClickListener() {
            recordVideo()
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                finishWithAnimation()
            }

            R.id.action_submit_post -> {
                submitPost(item)
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishWithAnimation()
    }

    private fun submitPost(item: MenuItem): Boolean {
        if (_contentInput.text.toString().isEmpty()) {
            Toast.makeText(this, "Post can't empty", Toast.LENGTH_SHORT).show()
            return true
        }

        item.isEnabled = false

        _model.addPost(
            content = _contentInput.text.toString(),
            media_type = _mediaType,
            media_uri = _mediaUri
        ).observe(this, Observer {
            if (it != null) {
                val intent = Intent()
                intent.putExtra("post", Gson().toJson(it))
                setResult(Activity.RESULT_OK, intent)
                finishWithAnimation()
            }
        })

        return true
    }


    private fun selectMedia() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        val mimetypes = arrayOf("image/*", "video/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        startActivityForResult(intent, REQUEST_MEDIA_PICKER_SELECT)
    }

    private fun takePhoto() {

    }

    private fun recordVideo() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                RequestCodes.REQUEST_MEDIA_PICKER_SELECT -> {
                    if (resultCode == Activity.RESULT_OK && data != null) {

                        _mediaUri = data.data
                        Log.d("wae", _mediaUri?.path!!)

                        if (_mediaUri.toString().contains("image")) {
                            _mediaType = MediaType.IMAGE
                            _postAddImageView.visibility = View.VISIBLE
                            _postAddVideoView.visibility = View.GONE

                            Glide.with(this).load(_mediaUri)
                                .into(_postAddImageView)

                        } else if (_mediaUri.toString().contains("video")) {
                            _mediaType = MediaType.VIDEO
                            _postAddImageView.visibility = View.GONE
                            _postAddVideoView.visibility = View.VISIBLE
                            _postAddVideoView.setVideoURI(_mediaUri)


                        }
                    }
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun finishWithAnimation() {
        finish()
        overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
    }
}