package com.example.storyapp.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoryAppRepository
import com.example.storyapp.data.response.Story
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: StoryAppRepository) : ViewModel() {
    fun stories(token: String): LiveData<PagingData<Story>> =
        repository.getStories(token).cachedIn(viewModelScope)
}