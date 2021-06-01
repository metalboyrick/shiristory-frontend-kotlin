package com.example.shiristory.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.shiristory.R

class StoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        actionBar?.setDisplayHomeAsUpEnabled(true);

        val storyIntent = intent
        val msg: String? = storyIntent.getStringExtra("storyId")
        if (msg != null) {
            Log.d("story", msg)
        }
    }
}