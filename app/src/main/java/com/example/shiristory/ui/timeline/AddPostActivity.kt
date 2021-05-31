package com.example.shiristory.ui.timeline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.shiristory.R
import com.example.shiristory.service.common.MediaType
import com.example.shiristory.service.common.ToolKits
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.rengwuxian.materialedittext.MaterialEditText


class AddPostActivity : AppCompatActivity() {

    private lateinit var _contentInput: MaterialEditText
    private var _mediaType: MediaType? = null
    private var _mediaUri: Uri? = null
    private val _model: TimelineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val selectMediaButton: Button = findViewById(R.id.post_add_select_media_button)
        val takePhotoButton: ImageButton = findViewById(R.id.post_add_take_photo_button)
        val recordVideoButton: ImageButton = findViewById(R.id.post_add_record_video_button)
        _contentInput = findViewById(R.id.post_add_input)

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

            android.R.id.home ->{
                finishWithAnimation()
            }

            R.id.action_submit_post -> {

                if (_contentInput.text.toString().isEmpty()){
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
                        setResult(Activity.RESULT_OK, intent)
                        finishWithAnimation()
                    }
                })

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

    private fun selectMedia() {

    }

    private fun takePhoto() {

    }

    private fun recordVideo() {

    }

    private fun finishWithAnimation(){
        finish()
        overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
    }
}