package com.example.jobapplication.models

data class Chat(
    val sender: String = "",
    val receiver: String = "",
    val message: String = "",
    val isSeen: Boolean = false
)
