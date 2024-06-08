package com.example.storyapp.view.model

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryAppRepository
import java.io.File

class UploadStroyViewModel(private val repository: StoryAppRepository) : ViewModel() {
    fun UploadStory(file: File, description: String, token: String, lat: Double, lon: Double) = repository.uploadStory(file, description, token, lat, lon)
}