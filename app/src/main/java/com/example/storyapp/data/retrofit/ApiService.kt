package com.example.storyapp.data.retrofit

import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoryResponse
import com.example.storyapp.data.response.StoryResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Header("Authorization") token: String
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double,
        @Header("Authorization") token: String
    ): StoryResult

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") loc: Int,
        @Header("Authorization") token: String
    ): StoryResponse
}