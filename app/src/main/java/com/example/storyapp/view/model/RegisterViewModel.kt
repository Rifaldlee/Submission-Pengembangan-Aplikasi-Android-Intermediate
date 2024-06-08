package com.example.storyapp.view.model

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryAppRepository

class RegisterViewModel (private val storyRepository: StoryAppRepository) : ViewModel() {
    fun signUp(name: String, email: String, password: String) = storyRepository.register(name, email, password)
}