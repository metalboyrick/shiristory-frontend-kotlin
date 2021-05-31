package com.example.shiristory.ui.timeline

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.shiristory.R


class AddPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val selectMediaButton: Button = view.findViewById(R.id.post_add_select_media_button)
        val takePhotoButton: ImageButton = view.findViewById(R.id.post_add_take_photo_button)
        val recordVideoButton: ImageButton = view.findViewById(R.id.post_add_record_video_button)
    
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_post_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
    }

    private fun addMedia(){

    }
}