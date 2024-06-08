package com.example.storyapp.view.model

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryAppRepository

class MapsViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {
    fun getStories(location: Int, token: String) = storyRepository.getStoriesLocation(location, token)
}