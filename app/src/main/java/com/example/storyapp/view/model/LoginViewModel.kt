package com.example.storyapp.view.model

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryAppRepository

class LoginViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {
    fun login(email: String, password: String) = storyRepository.login(email, password)
}