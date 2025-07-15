package com.example.kutlaapp.data.model

data class Contact(
    val name: String,
    val phoneNumber: String,
    val isKutlaUser: Boolean = false,
    val userId: String? = null
)