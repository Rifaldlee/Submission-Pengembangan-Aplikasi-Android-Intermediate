package com.example.storyapp.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginModel (
    var userId: String? = null,
    var name: String? = null,
    var token: String? = null
) : Parcelable