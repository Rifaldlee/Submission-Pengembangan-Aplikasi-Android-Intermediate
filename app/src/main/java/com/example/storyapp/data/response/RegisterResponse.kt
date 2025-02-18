package com.example.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?
)