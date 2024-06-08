package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.Story
import com.example.storyapp.data.response.StoryResponse
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.database.StoryDatabase
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryAppRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {

    fun register(name: String, email: String, pass: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, pass)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("register", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }
    fun login(email: String, pass: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, pass)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }
    fun getStories(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4
            ),
            pagingSourceFactory = {
                PagingSource(apiService, token)
            }
        ).liveData
    }
    fun uploadStory(imageFile: File, description: String, token: String, lat: Double, lon: Double) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.postStory(multipartBody, requestBody, lat, lon, token)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }
    fun getStoriesLocation(location: Int, token: String): LiveData<Result<StoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStoriesWithLocation(location, token)
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d("story_maps", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }
}