package com.example.shiristory.ui.timeline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shiristory.R


class AddPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
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
}