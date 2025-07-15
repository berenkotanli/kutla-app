package com.example.kutlaapp.data.model

import java.io.Serializable

data class UserProfile(
    val userId: String = "",
    val name: String = "",
    val birthDate: Long = 0L,
    val phone: String = "",
    val job: String = "",
    val hobbies: String = "",
    val gender: String = "",
    val profileImageUrl: String = ""
): Serializable
