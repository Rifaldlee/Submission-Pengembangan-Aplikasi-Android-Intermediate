package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.StoryAppRepository
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryAppRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(database, apiService)
    }
}