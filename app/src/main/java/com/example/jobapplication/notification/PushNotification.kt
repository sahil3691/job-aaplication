package com.example.buzzhub.notification

data class PushNotification(
    val data : NotificationData,
    val to : String? = ""
)
