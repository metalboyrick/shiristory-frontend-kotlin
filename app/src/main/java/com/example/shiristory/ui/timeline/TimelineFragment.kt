package com.example.shiristory.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shiristory.R

class TimelineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val model: TimelineViewModel by viewModels()
        val root = inflater.inflate(R.layout.fragment_timeline, container, false)
        return root
    }

}