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
import com.example.shiristory.service.common.MediaUtil
import com.example.shiristory.service.common.FileUtil
import com.example.shiristory.service.common.FileUtil.Companion.trimCache
import com.example.shiristory.service.common.MediaType
import com.example.shiristory.service.common.RequestCodes.REQUEST_MEDIA_CAMERA_CAPTURE
import com.example.shiristory.service.common.RequestCodes.REQUEST_MEDIA_PICKER_SELECT
import com.example.shiristory.service.common.RequestCodes.REQUEST_MEDIA_VIDEO_RECORD
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import java.io.File


class AddPostActivity : AppCompatActivity() {

    private val TAG = this.javaClass.name
    private val _model: TimelineViewModel by viewModels()

    private lateinit var _contentInput: MaterialEditText
    private lateinit var _postAddImageView: ImageView
    private lateinit var _postAddVideoView: VideoView

    private val _mediaUtil =
        MediaUtil(this)
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
            _mediaUtil.selectMedia()
        })
        takePhotoButton.setOnClickListener(View.OnClickListener() {
            _mediaUtil.takePhoto()
        })
        recordVideoButton.setOnClickListener(View.OnClickListener() {
            _mediaUtil.recordVideo()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        trimCache(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            REQUEST_MEDIA_PICKER_SELECT, REQUEST_MEDIA_VIDEO_RECORD -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    _mediaUri = data.data
                    Log.d(TAG, "raw media uri:" + _mediaUri?.path!!)

                    _mediaUri?.also {
                        if (it.toString().contains("image")) {
                            _mediaType = MediaType.IMAGE
                            _postAddImageView.visibility = View.VISIBLE
                            _postAddVideoView.visibility = View.GONE

                            Glide.with(this).load(it)
                                .into(_postAddImageView)

                        } else if (it.toString().contains("video")) {
                            _mediaType = MediaType.VIDEO
                            _postAddImageView.visibility = View.GONE
                            _postAddVideoView.visibility = View.VISIBLE
                            _postAddVideoView.setVideoURI(it)

                        }

                        val file: File = FileUtil.from(this, it)

                        _mediaUri = Uri.parse(file.path)
                        Log.d(TAG, "parsed media uri:" + _mediaUri?.path!!)
                    }
                }
            }

            REQUEST_MEDIA_CAMERA_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    _mediaUri = _mediaUtil.getPhotoUri()
                    _mediaType = MediaType.IMAGE
                    _postAddImageView.visibility = View.VISIBLE
                    _postAddVideoView.visibility = View.GONE

                    Glide.with(this).load(_mediaUri)
                        .into(_postAddImageView)

                    Log.d(TAG, "raw camera capture uri:" + _mediaUri?.path!!)
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

